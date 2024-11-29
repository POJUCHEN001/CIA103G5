package com.cia103g5.user.productImage.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import com.cia103g5.user.product.model.ProductVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="prod_image")
public class ProductImageVO {
	
	@Id
	@Column(name="image_no")
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Integer imageNo;
	
	
	@ManyToOne //FK
	@JoinColumn(name="prod_no",referencedColumnName = "prod_no") //前者為對應FK欄位名稱，後者為PK欄位名稱
	private ProductVO productVO;
	
	@Column(name="prod_pic",columnDefinition = "longblob")
	private byte[] prodPic;
	
	
	@Column(name="is_primary",nullable=false)
	private Byte isPrimary;
	
	@Column(name="created_time",updatable=false)
	private Timestamp createdTime;

	public ProductImageVO() {
		super();
	}




	
public ProductImageVO(Integer imageNo, ProductVO productVO, byte[] prodPic, Byte isPrimary, Timestamp createdTime) {
		super();
		this.imageNo = imageNo;
		this.productVO = productVO;
		this.prodPic = prodPic;
		this.isPrimary = isPrimary;
		this.createdTime = createdTime;
	}


public Integer getImageNo() {
		return imageNo;
	}

	public void setImageNo(Integer imageNo) {
		this.imageNo = imageNo;
	}

	public ProductVO getProductVO() {
		return productVO;
	}

	public void setProductVO(ProductVO productVO) {
		this.productVO = productVO;
	}

	public byte[] getProdPic() {
		return prodPic;
	}

	public void setProdPic(byte[] prodPic) {
		this.prodPic = prodPic;
	}

	public Byte getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Byte isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	//	將資料庫取出的照片寫到指定資料夾中
	public void writePic(byte[] buf,Integer prod_no) throws IOException {
		File file =new File("src\\main\\resources\\static\\images\\testpic."+prod_no+".jpg");
		try {
			FileOutputStream fos =new FileOutputStream(file);
			BufferedOutputStream bos =new BufferedOutputStream(fos);
			
			bos.write(buf);
			
			
			bos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
//將圖片資料(路徑)傳入，轉換成byte[]並回傳->準備送進資料庫
	public byte[] readPic(String filepath) throws IOException {
		File file =new File(filepath);
		FileInputStream fis=new FileInputStream(file);
		BufferedInputStream bos =new BufferedInputStream(fis);
	
		byte[] buffer =new byte[bos.available()];
		buffer=bos.readAllBytes();
		
		bos.close();
		fis.close();
		
		return buffer;
	} 
	

	
	
}
