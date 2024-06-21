package com.example.app.controller;

import java.io.File;
import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.ArtInfo;
import com.example.app.domain.Event;
import com.example.app.domain.NurseryReservation;
import com.example.app.domain.User;
import com.example.app.domain.UserNurseryReservation;
import com.example.app.domain.UserReservation;
import com.example.app.mapper.ArtInfoMapper;
import com.example.app.mapper.NurseryReservationMapper;
import com.example.app.mapper.UserReservationMapper;
import com.example.app.service.AdminService;
import com.example.app.service.MainPageService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ManageController {

	private static final String UPLOAD_DIRECTORY = "C:/Users/zd3M12/gallerys/";
	private final ArtInfoMapper artInfoMapper;
	private final AdminService userService;
	private final UserReservationMapper reservationMapper;
	private final MainPageService mainPageService;
	private final NurseryReservationMapper nurseryReservationMapper;

	// DB:ArtPiece管理ページ
	@GetMapping("/artPieceList")
	public String uploadGet(Model model) throws Exception {
		model.addAttribute("ArtList", artInfoMapper.showArtPieces());
		model.addAttribute("artInfo", new ArtInfo());
		return "back/artPieces";
	}

	@PostMapping("/uploadPict")
	public String post(@RequestParam MultipartFile upfile, @ModelAttribute ArtInfo artInfo, Model model)
			throws Exception {
		if (!upfile.isEmpty()) {
			// 元のファイル名を取得
			String fileName = upfile.getOriginalFilename();

			// uploadsフォルダに保存
			File dest = new File(UPLOAD_DIRECTORY + fileName);
			upfile.transferTo(dest);

			// ArtInfoオブジェクトを更新
			artInfo.setFileName(fileName);

			// ArtInfoをDBに入れる。
			artInfoMapper.addArtPiece(artInfo);

		}
		// ファイル未選択：リダイレクトによりGetリクエストにする
		return "redirect:/artPieceList";
	}

	@PostMapping("/updateArtPiece")
	public String post(@ModelAttribute ArtInfo artInfo, Model model) throws Exception {

		artInfoMapper.updateArtPiece(artInfo);
		return "redirect:/artPieceList";
	}

	@PostMapping("/manager/delete")
	public String post(@RequestParam("id") Integer id, Model model) throws Exception {
		artInfoMapper.deleteArtPiece(id);
		return "redirect:/artPieceList";
	}

	// DB:User管理ページ記述。
	@GetMapping("/userList")
	public String uploadUserList(Model model) throws Exception {
		List<User> userList = userService.selectAll();
		model.addAttribute("userList", userList);
		model.addAttribute("user", new User());
		return "back/user";
	}

	@PostMapping("/updateUser")
	public String updateUser(@Valid User user, Errors errors, Model model, RedirectAttributes redirectAttributes) throws Exception {
	    if (errors.hasErrors()) {
	        redirectAttributes.addFlashAttribute("updateErrorMessage", "入力エラーがあります。");
	        return "redirect:/userList";
	    }
	    if (user == null) {
	        redirectAttributes.addFlashAttribute("updateErrorMessage", "User cannot be null");
	        return "redirect:/userList";
	    } else {
	        userService.updateUser(user);
	    }
	    return "redirect:/userList";
	}

	@PostMapping("/deleteUser")
	public String deleteUser(@RequestParam(value = "id", required = false) Integer id, Model model, RedirectAttributes redirectAttributes) throws Exception {
	    if (id == null) {
	        redirectAttributes.addFlashAttribute("deleteErrorMessage", "IDが指定されていません。");
	        return "redirect:/userList";
	    } else {
	        userService.delete(id);
	    }
	    return "redirect:/userList";
	}


	// DB:Reservation管理ページ記述
	@GetMapping("/reservationList")
	public String uploadReservationList(Model model) throws Exception {
		List<UserReservation> userReservationList = reservationMapper.selectAll();
		UserReservation userReservation = new UserReservation();
		userReservation.setReservationDate(new Date(System.currentTimeMillis())); // 現在の日付を設定
		model.addAttribute("userReservation", userReservation);
		model.addAttribute("userReservationList", userReservationList);
		return "back/reservation";
	}

	@PostMapping("/updateReservation")
	public String updateReservation(@Valid UserReservation userReservation, Errors errors,
	        @RequestParam("reservationDate") String strReservationDate,
	        Model model, HttpSession session, RedirectAttributes redirectAttributes)
	        throws Exception {
	    if (errors.hasErrors()) {
	        redirectAttributes.addFlashAttribute("updateErrorMessage", "入力エラーがあります。");
	        return "redirect:/reservationList";
	    }
	    if (strReservationDate == null || strReservationDate.isEmpty()) {
	        redirectAttributes.addFlashAttribute("updateErrorMessage", "選択された日付が空です。");
	    }
	    return "redirect:/reservationList";
	}

	@PostMapping("/deleteReservation")
	public String deleteReservation(@RequestParam(value = "reservationId", required = false) Integer reservationId, Model model, RedirectAttributes redirectAttributes) throws Exception {
	    if (reservationId == null) {
	        redirectAttributes.addFlashAttribute("deleteErrorMessage", "予約IDが指定されていません。");
	        return "redirect:/reservationList";
	    } else {
	        reservationMapper.deleteReservation(reservationId);
	    }
	    return "redirect:/reservationList";
	}

	@GetMapping("/getEventName")
	@ResponseBody
	public String getEventName(@RequestParam("id") Integer id) throws Exception {
		Event event = mainPageService.getEventById(id);
		return event != null ? event.getEventName() : "";
	}

	@GetMapping("/getUserName")
	@ResponseBody
	public String getUserName(@RequestParam("id") Integer id) throws Exception {
		User user = userService.selectById(id);
		return user != null ? user.getUserName() : "";
	}

	@GetMapping("/nurseryReservationList")
	public String showNurseryReservation(Model model) throws Exception {
		List<UserNurseryReservation> nurseryReservationList = nurseryReservationMapper.showNurseryReservationList();
		model.addAttribute("nurseryReservationList", nurseryReservationList);
		model.addAttribute("nurseryReservation", new NurseryReservation());
		return "back/nurseryReservation";
	}

	@PostMapping("/updateNurseryReservation")
	public String updateNurseryReservation(@ModelAttribute @Valid NurseryReservation nurseryReservation, Errors errors, Model model, RedirectAttributes redirectAttributes) throws Exception {
	    if(errors.hasErrors()) {
	        redirectAttributes.addFlashAttribute("updateErrorMessage", "入力エラーがあります。");
	        return "redirect:/nurseryReservationList";
	    }
	    nurseryReservationMapper.updateNurseryReservation(nurseryReservation);
	    model.addAttribute("nurseryReservation", nurseryReservation);
	    return "redirect:/nurseryReservationList";
	}
}
