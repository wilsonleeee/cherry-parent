package com.cherry.webserviceout.couponcode;

import java.util.Map;

public interface ISynchroCodeStatus {
	Map<String,Object> synchroCodeStatus(String brandCode,Map<String,Object> param) throws Exception;
}
