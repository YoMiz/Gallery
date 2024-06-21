package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.ArtInfo;

@Mapper
public interface ArtInfoMapper {
	List<ArtInfo> showArtPieces();
	void addArtPiece(ArtInfo artInfo)throws Exception;
	List<ArtInfo> selectByEventId(Integer eventId) throws Exception;
	void deleteArtPiece(Integer id)throws Exception;
	void updateArtPiece(ArtInfo artInfo)throws Exception;
}
