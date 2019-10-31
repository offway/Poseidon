package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.*;
import cn.offway.Poseidon.service.*;
import cn.offway.Poseidon.domain.*;
import cn.offway.Poseidon.service.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单查询
 * @author wn
 *
 */
@Controller
public class OrderController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhOrderInfoService phOrderInfoService;
	
	@Autowired
	private PhOrderGoodsService phOrderGoodsService;
	
	@Autowired
	private PhOrderExpressInfoService phOrderExpressInfoService;
	
	@Autowired
	private PhGoodsStockService phGoodsStockService;
	
	@Autowired
	private PhBrandService phBrandService;

	@Autowired
	private PhOrderRemarkService phOrderRemarkService;

	@Autowired
	private PhOrderService phOrderService;
	
	
	@RequestMapping("/order.html")
	public String order(ModelMap map,Authentication authentication,String brandId){
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();
		
		map.addAttribute("brands", phBrandService.findByIds(brandIds));
		map.addAttribute("brandId",brandId);
		return "order";
	}
	
	@RequestMapping("/order-return.html")
	public String orderReturn(ModelMap map,Authentication authentication){
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();
		
		map.addAttribute("brands", phBrandService.findByIds(brandIds));
		return "order-return";
	}
	
	/**
	 * 查询数据
	 */
	@ResponseBody
	@RequestMapping("/order-data")
	public Map<String, Object> orderData(HttpServletRequest request, String userId, String orderNo, String status, Authentication authentication) {
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();

		PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Direction.fromString(sortDir), sortName);
		Page<PhOrder> pages = phOrderService.findAllByPage(userId,orderNo,status,pr);
		// 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;  
        Map<String, Object> map = new HashMap<>();
		map.put("sEcho", initEcho);  
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数  
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数  
        map.put("aData", pages.getContent());//数据集合 
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/order-goods")
	public List<PhOrderGoods> phOrderGoods(String orderNo){
		return phOrderGoodsService.findByOrderNo(orderNo);
	}
	
	@ResponseBody
	@RequestMapping("/order-express")
	public PhOrderExpressInfo phOrderExpress(String orderNo, String type){
		return phOrderExpressInfoService.findByOrderNoAndType(orderNo, type);
	}
	
	@ResponseBody
	@RequestMapping("/order-check")
	public boolean orderCheck(String orderNo){
		try {
			return phGoodsStockService.updateStock(orderNo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("order-check异常orderNo:{}",orderNo,e);
			return false;
		}
		
	}
	
	@ResponseBody
	@RequestMapping("/order-update")
	public boolean orderUpdate(String orderNo,String status,Authentication auth){
		try {
			PhOrderInfo phOrderInfo = phOrderInfoService.findByOrderNo(orderNo);
			phOrderInfo.setStatus(status);
			phOrderInfo.setRemark("修改订单状态为："+status+",修改时间："+DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")+",修改人："+auth.getName());
			phOrderInfoService.save(phOrderInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("order-update异常orderNo:{},status:{}",orderNo,status,e);
			return false;
		}
		
	}

	@ResponseBody
	@RequestMapping("order-addremark")
	public boolean addremark(String id,String content){
		try {
			PhOrderRemark orderRemark = new PhOrderRemark();
			PhOrderInfo orderInfo = phOrderInfoService.findOne(Long.valueOf(id));
			orderRemark.setContent(content);
  			orderRemark.setCreateTime(new Date());
			orderRemark.setOrdersNo(orderInfo.getOrderNo());
			orderRemark.setOrdersId(id);
			phOrderRemarkService.save(orderRemark);
			orderInfo.setExtra("1");
			phOrderInfoService.save(orderInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("order-addremark异常id:{},content:{}",id,content,e);
			return false;
		}
	}
	
	@ResponseBody
	@RequestMapping("order-remarkbyid")
	public Map<String,Object> remarkbyid(HttpServletRequest request,String id){
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));

		Page<PhOrderRemark> pages = phOrderRemarkService.findAllByPage(id,new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
		// 为操作次数加1，必须这样做
		int initEcho = sEcho + 1;
		Map<String, Object> map = new HashMap<>();
		map.put("sEcho", initEcho);
		map.put("iTotalRecords", pages.getTotalElements());//数据总条数
		map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
		map.put("aData", pages.getContent());//数据集合
		return map;
	}

	@ResponseBody
	@RequestMapping("order-delremark")
	public boolean delremark(Long id){
		phOrderRemarkService.delete(id);
		return true;
	}
	
	
}
