package com.cherry.cp.common.bl;

import com.cherry.cm.util.ConvertUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by menghao on 2017/1/11.
 *
 * 背景:取随机数的逻辑存在两个问题:
 *      1:BigInteger.probablePrime方法取到的数存在取到非质数的概率,尤其是在小数据量时概率尤其高.
 *      2:改进后的逻辑:
 *              1)对P\Q\E都做了质数判断,若非质数则重新取数.
 *              2)取不同位数的类都有一个总数的判断,如:取6位,则随机数大于999999则舍弃后重新再取.
 * 改进的逻辑缺陷:
 *      因为舍弃了大于指定位数的随机数,故可能存在取数小于指定数的情况,
 *      此时需要用此类进行封闭,对于取数不足的部分再次调用方法进行取数直到数据取满为止.
 */
public class BINOLCPCOMCOUPON_APPLY_BL {

    @Resource(name="binolcpcomcoupon6bl")
    private BINOLCPCOMCOUPON_6_BL binolcpcomcoupon6bl;

    /**
     *
     * @param map
     * //@param organizationInfoId
     * //@param brandInfoId
     * //@param campCode
     * //@param couponCount
     * @return
     * @throws Exception
     */
    public List<String> generateRandomCode6(Map<String, Object> map)
            throws Exception {
        List<String> resultList = new ArrayList<String>();

        while(true) {
            List<String>  generateCouponList= binolcpcomcoupon6bl.generateCoupon(map);
            int couponCount = ConvertUtil.getInt(map.get("couponCount"));
            if(generateCouponList == null) {
                break;
            } else if(generateCouponList.size() < couponCount) {
                map.put("couponCount", couponCount-generateCouponList.size());
                resultList.addAll(generateCouponList);
            } else {
                resultList.addAll(generateCouponList);
                break;
            }
        }

        return resultList;
    }

}
