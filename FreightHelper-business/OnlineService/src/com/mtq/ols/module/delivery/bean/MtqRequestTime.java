package com.mtq.ols.module.delivery.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

@Table("request_time")
public class MtqRequestTime {
	
	@PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("_id")
    public long id;
	/**客户单号*/
	@Column("_cust_order_id")
	public String cust_orderid;
	/**要求最晚送达时间*/
	@Column("_req_time_e")
	public long req_time_e;
	
}
