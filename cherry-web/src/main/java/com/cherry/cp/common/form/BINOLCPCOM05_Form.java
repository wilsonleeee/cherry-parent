package com.cherry.cp.common.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLCPCOM05_Form extends DataTable_BaseForm{

		private int times;
		
		private String subCampCode;
		
		private String checkType;

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}

		public String getSubCampCode() {
			return subCampCode;
		}

		public void setSubCampCode(String subCampCode) {
			this.subCampCode = subCampCode;
		}

		public String getCheckType() {
			return checkType;
		}

		public void setCheckType(String checkType) {
			this.checkType = checkType;
		}
}
