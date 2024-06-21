package com.example.app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Event;
import com.example.app.domain.EventData;
import com.example.app.domain.ModelData;
import com.example.app.domain.User;
import com.example.app.domain.UserReservation;
import com.example.app.service.AdminService;
import com.example.app.service.MainPageService;
import com.example.app.service.MemberReservationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/gallery")
@RequiredArgsConstructor
public class MainController {
    private final MainPageService mainPageService;
    private final AdminService adminService;
    private final MemberReservationService memberReservationService;

    private void prepareModel(Integer page, Model model) throws Exception {
        ModelData modelData = mainPageService.prepareModelData(page);
        model.addAttribute("eventList", modelData.getEventList());
        model.addAttribute("events", modelData.getEvents());
        model.addAttribute("page", modelData.getPage());
        model.addAttribute("totalPages", modelData.getTotalPages());
        model.addAttribute("admin", modelData.getAdmin());
        model.addAttribute("reservation", modelData.getReservation());
        if (modelData.getErrorMessage() != null) {
            model.addAttribute("errorMessage", modelData.getErrorMessage());
        }
    }

    private void prepareEvent(Integer eventId, HttpSession session, Model model) throws Exception {
        EventData eventData = mainPageService.prepareEventData(eventId);
        session.setAttribute("eventId", eventData.getEventId());
        model.addAttribute("currentEvent", eventData.getCurrentEvent());
        model.addAttribute("artInfoList", eventData.getArtInfoList());
        // model.addAttribute("pageStyle", eventData.getPageStyle());  // ページスタイルの数値をモデルに追加
        if (eventData.getTitleImg() != null) {
            model.addAttribute("titleImg", eventData.getTitleImg());
        }
    }


    @GetMapping
    public String main(@RequestParam(name = "page", defaultValue = "1") Integer page, HttpSession session, Model model)
            throws Exception {
        prepareModel(page, model);
        Integer lastEventId = mainPageService.getLastEventId();
        Integer currentEventId = lastEventId - 1;
        prepareEvent(currentEventId, session, model);
        session.setAttribute("page", page);  // ページ番号をセッションに保存
        return "redirect:/gallery/" + currentEventId;
    }

    @GetMapping("/{eventId}")
    public String showEvent(@PathVariable("eventId") Integer eventId,
            HttpSession session, Model model)
            throws Exception {
        Integer page = (Integer) session.getAttribute("page");  // セッションからページ番号を取得
        if (page == null) {
            page = 1;  // ページ番号がセッションに存在しない場合はデフォルト値を使用
        }
        Event event = mainPageService.getEventById(eventId);
        int pageStyle = event.getPageStyle();
        model.addAttribute("pageStyle", pageStyle);
        prepareModel(page, model);
        prepareEvent(eventId, session, model);
        return "front/main";
    }

	@PostMapping("/member")
	public String memberMain(@Valid User admin, Errors errors, HttpSession session, Model model) throws Exception {

	    if (errors.hasErrors()) {
	        return "redirect:/gallery";
	    }
	    if (!adminService.isCorrectIdAndPassword(admin.getLoginId(), admin.getPassword())) {
	        errors.rejectValue("loginId", "error.incorrect_id_password");
	        return "redirect:/gallery";
	    }

	    User user = adminService.selectByLoginId(admin.getLoginId());

	    //TODO
	    //userIdから最後の予約情報を取得。0の場合、現在進行中、１の場合出力しない。

	    session.setAttribute("user", user);
	    session.setAttribute("loginId", user.getLoginId());
	    session.setAttribute("isBeforeToday", false); // セッションに保存

	    model.addAttribute("admin", user);
	    model.addAttribute("reservation", new UserReservation());

	    return "redirect:/gallery";
	}
	@PostMapping("/selectedDate")
	public String showEventByDate(@RequestParam(value="selectedDate", required = false) String selectedDateString, Model model,
	        HttpSession session) throws Exception {

	    if(selectedDateString == null || selectedDateString.isEmpty()) {
	        session.setAttribute("errorMessage", "選択された日付が空です。");
	        return "redirect:/gallery";
	    }

	    java.sql.Date selectedDate;
	    try {
	        java.util.Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedDateString);
	        selectedDate = new java.sql.Date(parsedDate.getTime());
	    } catch (ParseException e) {
	        session.setAttribute("errorMessage", "無効な日付形式です。");
	        return "redirect:/gallery";
	    }

	    // 今日の日付を取得
	    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

	    // 選択された日付が今日よりも前であるかどうかをチェック
	    boolean isBeforeToday = selectedDate != null && selectedDate.before(today);
	    session.setAttribute("isBeforeToday", isBeforeToday); // セッションに保存
	    session.setAttribute("selectedDate", selectedDate); // ここでselectedDateをセッションに保存

	    Event currentEvent = null;
	    try {
	        currentEvent = mainPageService.findEventByDate(selectedDate);
	    } catch (Exception e) {
	        session.setAttribute("errorMessage", "日付に対応するイベントが見つかりませんでした。");
	        return "redirect:/gallery";
	    }

	    Integer eventId = currentEvent.getId();
	    if (eventId == null) {
	        session.setAttribute("errorMessage", "イベントがありません。");
	        return "redirect:/gallery";
	    }else {
	    	session.setAttribute("errorMessage", null);
	    }

	    session.setAttribute("selectedDate", selectedDate);
	    session.setAttribute("eventId", eventId); // eventIdをセッションに保存
	    model.addAttribute("reservation", new UserReservation());
	    return "redirect:/gallery/" + eventId;
	}


	@PostMapping("/reservation")
	public String makeReservation(@ModelAttribute UserReservation reservation,
	        HttpSession session,RedirectAttributes redirectAttributes,
	        @RequestParam("price") Integer price,
	        Model model) throws Exception {
	    Integer eventId = (Integer) session.getAttribute("eventId"); // セッションからeventIdを取得
	    User user = (User) session.getAttribute("user"); // セッションからuserを取得
	    if (user == null) {
	        redirectAttributes.addFlashAttribute("errorMessage", "ユーザー情報が見つかりません。");
	        return "redirect:/gallery";
	    }
	    if (session.getAttribute("selectedDate") == null) {
	        redirectAttributes.addFlashAttribute("errorMessage", "選択された日付がありません。");
	        return "redirect:/gallery";
	    }
	    java.sql.Date selectedDate = (java.sql.Date) session.getAttribute("selectedDate"); // ここでセッションからselectedDateを取得
	    reservation.setSelectedDate(selectedDate);

	    reservation.setUserId(user.getId());
	    reservation.setEventId(eventId);

	    //金額が0の場合、エラーメッセージをhtmlに返す
	    if(price == 0) {
	        redirectAttributes.addFlashAttribute("errorMessage", "人数を指定してください。");
	        return "redirect:/gallery/" + eventId;
	    }

	    memberReservationService.makeReservation(user, reservation);
	    
	    model.addAttribute("reservation", new UserReservation());
	    // リダイレクト
	    return "redirect:/gallery/" + eventId;
	}
	@GetMapping("/myPage")
	public String goToMyPage()throws Exception{
		return "redirect:/facility";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) throws Exception {
		session.invalidate();
		return "redirect:/gallery";
	}

}
