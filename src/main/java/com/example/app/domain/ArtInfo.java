package com.example.app.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtInfo {
	private Integer id;
	
	@NotBlank(message="展示された回を入力してください")
	private Integer eventId;
	
	@NotBlank(message="作品名を入力してください")
	@Size(max=60, message="５０文字以内で入力してください")
	private String title;
	
	@NotBlank(message="作者名を入力してください")
	@Size(max=60, message="６０文字以内で入力してください")
	private String author;

	private Integer height;

	@NotBlank(message="画像表示する幅を入力してください")
	private Integer width;

	@NotBlank(message="画像表示位置を入力してください")	
	private Integer position;

	@NotBlank(message="説明文を入力してください")
	@Size(max=500, message="５００文字以内で入力してください")
	private String description;
	
	@Size(max=60, message="６０文字以内で入力してください")
	private String fileName;

}
