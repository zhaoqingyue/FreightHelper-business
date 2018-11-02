package com.mtq.bus.freighthelper.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressBean implements Parcelable {
	public String kcode;
	public double x;
	public double y;
	public String getKcode() {
		return kcode;
	}

	public void setKcode(String kcode) {
		this.kcode = kcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String address;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(kcode);
		dest.writeString(address);
		dest.writeDouble(x);
		dest.writeDouble(y);
	}

	public static final Parcelable.Creator<AddressBean> CREATOR = new Creator<AddressBean>() {

		@Override
		public AddressBean createFromParcel(Parcel source) {

			AddressBean mPerson = new AddressBean();
			mPerson.kcode = source.readString();
			mPerson.address = source.readString();
			mPerson.x=source.readDouble();
			mPerson.y=source.readDouble();
			return mPerson;
		}

		@Override
		public AddressBean[] newArray(int size) {
			return new AddressBean[size];
		}
	};
}
