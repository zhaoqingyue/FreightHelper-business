package com.mtq.ols.module.delivery.bean;

import java.util.ArrayList;
import java.util.List;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Mapping;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;
import com.litesuits.orm.db.enums.Relation;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.Goods;

/**
 * 运单详情实体类
 * @author ligangfan
 *
 */
@Table("orderdetail")
public class MtqDeliOrderDetail {

	@PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("_id")
    public long id;
	/** 客户单号**/
	@Column("_cust_orderid")
	public String cust_orderid;
	/** 要求送达时间(起)**/
	@Column("_reqtime")
	public  long reqtime;
	/** 要求送达时间(止)**/
	@Column("_reqtime_e")
	public  long reqtime_e;
	/** 发货人**/
	@Column("_send_name")
	public String send_name;
	/** 发货人电话**/
	@Column("_send_phone")
	public String send_phone;
	/** 发货人地址 **/
	@Column("_send_addr")
	public String send_addr;
	/** 发货人K码 **/
	@Column("_send_kcode")
	public String send_kcode;
	/** 收货人**/
	@Column("_receive_name")
	public String receive_name;
	/** 收货人电话**/
	@Column("_receive_phone")
	public String receive_phone;
	/** 收货人地址**/
	@Column("_receive_addr")
	public String receive_addr;
	/** 收货人K码**/
	@Column("_receive_kcode")
	public String receive_kcode;
	/** 托运须知**/
	@Column("_note")
	public String note;
	/** 已上传照片数 **/
	@Column("_photo_nums")
	public int photo_nums;
	/** 已上传回单数 **/
	@Column("_receipt_nums")
	public int receipt_nums;
	/** 可上传照片数 **/
	@Column("_can_photo_nums")
	public int can_photo_nums;
	/** 可上传回单数 **/
	@Column("_can_receipt_nums")
	public int can_receipt_nums;
	
	
	/**已上传回单的图片编号（以小写“,”逗号）分隔**/
	@Column("_receipt_kcode")
	public String receipt_ids;
	/**已上传照片的图片编号（以小写“,”逗号）分隔*/
	@Column("_photo_kcode")
	public String photo_ids;
	
	@Column("_task_id")
	public String taskId;
	
	@Column("_corp_id")
	public String corpId;
	
	
	/** 货物**/
	@Mapping(Relation.OneToMany)
	public ArrayList<MtqGoodDetail> goods;
	
}
