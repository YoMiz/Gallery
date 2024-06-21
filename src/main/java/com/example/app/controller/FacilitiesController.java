package com.example.app.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.ModelData;
import com.example.app.domain.NurseryReservation;
import com.example.app.domain.QRCodeGenerator;
import com.example.app.domain.User;
import com.example.app.mapper.NurseryReservationMapper;
import com.example.app.service.MainPageService;
import com.example.app.service.MemberReservationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/facility")
@RequiredArgsConstructor
public class FacilitiesController {
	private final MainPageService mainPageService;
	private final MemberReservationService memberReservationService;
	private final NurseryReservationMapper nurseryReservationMapper;

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

	@GetMapping()
	public String showFacility(HttpSession session, Model model, @ModelAttribute("message") String message)
			throws Exception {
		Integer page = (Integer) session.getAttribute("page"); // セッションからページ番号を取得
		if (page == null) {
			page = 1; // ページ番号がセッションに存在しない場合はデフォルト値を使用
		}
		model.addAttribute("nurseryReservation", new NurseryReservation());
		prepareModel(page, model);

		// フラッシュ属性からメッセージを取得し、モデルに追加
		model.addAttribute("message", message);

		return "front/facilities";
	}

	@PostMapping("/nurseryReservation")
	public String makeNurseryReservation(
			@Valid @ModelAttribute("nurseryReservation") NurseryReservation nurseryReservation,
			BindingResult bindingResult, RedirectAttributes redirectAttrs, HttpSession session) throws Exception {

		Date today = new Date();
		Date reservationDate = nurseryReservation.getReservationDate();
		
		if (bindingResult.hasErrors()) {
			redirectAttrs.addFlashAttribute("nurseryError", "日付と予約数の入力が必要です");
			return "redirect:/facility";
		}
		if (reservationDate == null || reservationDate.before(today)) {
			redirectAttrs.addFlashAttribute("nurseryError", "未来の日付を入力してください");
			return "redirect:/facility";
		}

		User user = (User) session.getAttribute("user");
		nurseryReservation.setUserId((Integer) user.getId());

		Integer reservationNumber = nurseryReservation.getReservationNumber();
		Integer nurseryPrice = reservationNumber * 1000;
		nurseryReservation.setPrice(nurseryPrice);

		nurseryReservationMapper.makeNurseryReservation(nurseryReservation);
		return "redirect:/facility";
	}



	@PostMapping("/freeDrink")
	public String freeDrinkTicket(@RequestParam("drinkAmt") String strDrinkAmt, RedirectAttributes redirectAttrs,
			HttpSession session) throws Exception {
		User user = (User) session.getAttribute("user");
		Integer drinkAmt = Integer.parseInt(strDrinkAmt);
		String message;
		if (!memberReservationService.checkPoints(user, drinkAmt)) {
			message = "ポイントが足りません";
		} else {
			Integer usedPoint = drinkAmt * 500;
			message = usedPoint + "ポイント使用しました";
			memberReservationService.usePoints(user, drinkAmt);

			// QRコードを生成
			QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
			BufferedImage qrCodeImage = qrCodeGenerator.generateQRCodeImage("http://localhost:8080/facility/freeDrink");
			// QRコード画像をWebサーバー上に保存し、そのURLを取得
			String qrCodeImageUrl = saveQRCodeImageAndGetUrl(qrCodeImage);
			// QRコード画像のURLをリダイレクト先のページに渡す
			redirectAttrs.addFlashAttribute("qrCodeImageUrl", qrCodeImageUrl);
		}
		redirectAttrs.addFlashAttribute("message", message);
		return "redirect:/facility";
	}

	private String saveQRCodeImageAndGetUrl(BufferedImage qrCodeImage) {
		try {
			// 画像を保存するファイルのパスを指定します。
			// この例では、Spring Bootの静的リソースディレクトリに "qrcode.png" という名前のファイルを作成します。
			Path path = Paths.get("./src/main/resources/static/images/qrcode.png");

			// 画像をファイルに書き込みます。
			ImageIO.write(qrCodeImage, "PNG", path.toFile());

			// ファイルへのパスを URL として返します。
			// この例では、Spring Bootの静的リソースへのパスをそのまま返しています。
			return "/images/qrcode.png";
		} catch (IOException e) {
			// 画像の書き込みに失敗した場合は、エラーを通知します。
			throw new RuntimeException("Failed to save QR code image", e);
		}
	}
}
