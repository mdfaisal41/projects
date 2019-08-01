package com.pos.pointOfSale.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aes.protection.CipherUtils;
import com.pos.inventory.model.Inventory;
import com.pos.login.model.MenuInfo;
import com.pos.login.service.LoginService;
import com.pos.lookup.model.LookupModel;
import com.pos.lookup.service.LookupService;
import com.pos.pointOfSale.model.PointOfSale;
import com.pos.pointOfSale.service.PointOfSaleService;

@Controller
@RequestMapping("pointOfSale")
public class PointOfSaleController {

	CipherUtils oCipherUtils = new CipherUtils();

	@Autowired 
	private LoginService loginService;
	
	@Autowired
	private LookupService lookupService;
	@Autowired
	private PointOfSaleService pointOfSaleService;

	@RequestMapping(value = "createItem", method = RequestMethod.GET)
	public ModelAndView createItem(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			LookupModel lookupModel, final RedirectAttributes redirectAttributes) {
		
		if (session.getAttribute("logonSuccessYN") == "Y") {
			
			String menuId = "M0201";
			MenuInfo menuInfo = loginService.checkUserAuthorization((String) session.getAttribute("employeeid"),
					menuId);
			
			if (menuInfo.getMenuId().equals(menuId)) {
				
			ModelAndView mav = new ModelAndView();

			mav.setViewName("createItem");
			return mav;
			
			  } else {
					redirectAttributes.addFlashAttribute("message", "You are not authorized for this Page !");
					redirectAttributes.addFlashAttribute("mCode", "0000");
					return new ModelAndView("redirect:/");
				}
		} else {
			return new ModelAndView("login");
		}
	}
	

	@RequestMapping(value = "saveItem", method = RequestMethod.POST)
	public ModelAndView saveItem(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			final RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (session.getAttribute("logonSuccessYN") == "Y") {
			try {
				pointOfSale.setUpdateBy((String) session.getAttribute("employeeid"));
				PointOfSale oPointOfSale = new PointOfSale();

				oPointOfSale = pointOfSaleService.saveItem(pointOfSale);

				redirectAttributes.addFlashAttribute("message", oPointOfSale.getMessage());
				redirectAttributes.addFlashAttribute("mCode", oPointOfSale.getmCode());

				redirectAttributes.addFlashAttribute("pointOfSale", pointOfSale);
				return new ModelAndView("redirect:/pointOfSale/createItem");
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("mCode", "0000");
				redirectAttributes.addFlashAttribute("message", "Error occured!!");
				return new ModelAndView("redirect:/pointOfSale/createItem");
			}
		} else {
			return new ModelAndView("redirect:/login");
		}
	}
	

	@RequestMapping(value = "getItemList", method = RequestMethod.POST)
	public ModelAndView getProductList(@ModelAttribute PointOfSale pointOfSale, HttpSession session) throws Exception {

		if (session.getAttribute("logonSuccessYN") == "Y") {
			ModelAndView mav = new ModelAndView("itemList");
			List<PointOfSale> oItemList = pointOfSaleService.getItemList(pointOfSale);

			// System.out.println("em id "+hrInfo.getEmployeeId());
			// System.out.println("depart id "+hrInfo.getDepartmentId());

			if (oItemList.size() > 0) {
				//System.out.println("size " + oItemList.size());
				mav.addObject("itemList", oItemList);
			} else {
				mav.addObject("itemListNotFound", "No Item Found!");
			}

			return mav;
		} else {
			return new ModelAndView("redirect:/login");
		}

	}
	

	@RequestMapping(value = "itemConfiguration", method = RequestMethod.GET)
	public ModelAndView itemConfiguration(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			LookupModel lookupModel, final RedirectAttributes redirectAttributes) {
		
		if (session.getAttribute("logonSuccessYN") == "Y") {
			
			String menuId = "M0202";
			MenuInfo menuInfo = loginService.checkUserAuthorization((String) session.getAttribute("employeeid"),
					menuId);
			
			if (menuInfo.getMenuId().equals(menuId)) {
				
			ModelAndView mav = new ModelAndView();

			mav.setViewName("itemConfiguration");
			mav.addObject("itemList", lookupService.itemList(lookupModel));
			mav.addObject("unitList", lookupService.unitList(lookupModel));
			mav.addObject("productList", lookupService.productList(lookupModel));
			mav.addObject("employeeList", lookupService.employeeList(lookupModel));
			mav.addObject("inventoryTypeList", lookupService.inventoryTypeList(lookupModel));
			return mav;
			
		    } else {
						redirectAttributes.addFlashAttribute("message", "You are not authorized for this Page !");
						redirectAttributes.addFlashAttribute("mCode", "0000");
						return new ModelAndView("redirect:/");
					}
		} else {
			return new ModelAndView("login");
		}
	}
	

	@RequestMapping(value = "saveItemConfig", method = RequestMethod.POST)
	public @ResponseBody PointOfSale saveItemConfig(@ModelAttribute PointOfSale pointOfSale, LookupModel lookupModel,
			HttpSession session, final RedirectAttributes redirectAttributes) {
		// System.out.println("entrance");
		// System.out.println();
		PointOfSale oPointOfSale = new PointOfSale();
		if (session.getAttribute("logonSuccessYN") == "Y") {

			pointOfSale.setUpdateBy((String) session.getAttribute("employeeid"));

			oPointOfSale = pointOfSaleService.saveItemConfig(pointOfSale);

			redirectAttributes.addFlashAttribute("message", oPointOfSale.getMessage());
			redirectAttributes.addFlashAttribute("mCode", oPointOfSale.getmCode());
			redirectAttributes.addFlashAttribute("pointOfSale", pointOfSale);

		}
		return oPointOfSale;
	}
	

	@RequestMapping(value = "getItemConfigList", method = RequestMethod.POST)
	public ModelAndView getItemConfigList(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			LookupModel lookupModel, final RedirectAttributes redirectAttributes) {

		if (session.getAttribute("logonSuccessYN") == "Y") {
			ModelAndView mav = new ModelAndView("itemList");
			try {

				// taskTrackingInfo.setEmployeeId((String)
				// session.getAttribute("employeeId"));

				List<PointOfSale> oItemConfigList = pointOfSaleService.getItemConfigList(pointOfSale);

				if (oItemConfigList.size() > 0) {
					mav.addObject("itemConfigList", oItemConfigList);
				} else {
					mav.addObject("itemConfigListNotFound", "No Data Found!");
				}
				mav.addObject("productList", lookupService.productList(lookupModel));
				mav.addObject("unitList", lookupService.unitList(lookupModel));
				// lookupService.projectsList(lookupModel));
				// mav.addObject("taskTypeList", lookupService.taskTypeList());

				return mav;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return new ModelAndView("redirect:/");
		}
	}
	

	@RequestMapping(value = "orderManagement", method = RequestMethod.GET)
	public ModelAndView orderManagement(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			LookupModel lookupModel, final RedirectAttributes redirectAttributes) {
		
		if (session.getAttribute("logonSuccessYN") == "Y") {

			String menuId = "M0203";
			MenuInfo menuInfo = loginService.checkUserAuthorization((String) session.getAttribute("employeeid"),
					menuId);
			
			if (menuInfo.getMenuId().equals(menuId)) {
			
			ModelAndView mav = new ModelAndView();

			mav.setViewName("orderManagement");
			mav.addObject("itemList", lookupService.itemList(lookupModel));
			
			List<PointOfSale> oPendingOrderList = pointOfSaleService.getPendingOrderList(pointOfSale);

			if (oPendingOrderList.size() > 0) {
				mav.addObject("pendingOrderList", oPendingOrderList);
			} else {
				mav.addObject("pendingOrderListNotFound", "No pending Order Found!");
			}
			
			
			//mav.addObject("unitList", lookupService.unitList(lookupModel));
			//mav.addObject("productList", lookupService.productList(lookupModel));
			//mav.addObject("employeeList", lookupService.employeeList(lookupModel));
			//mav.addObject("inventoryTypeList", lookupService.inventoryTypeList(lookupModel));
			
			
			
			return mav;
			
			} else {
				redirectAttributes.addFlashAttribute("message", "You are not authorized for this Page !");
				redirectAttributes.addFlashAttribute("mCode", "0000");
				return new ModelAndView("redirect:/");
			}
		} else {
			return new ModelAndView("login");

		}
	}
	

	@RequestMapping(value = "getOrderInfoSList", method = RequestMethod.POST)
	public @ResponseBody PointOfSale getOrderInfoSList(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			RedirectAttributes redirectAttributes) throws Exception {
		// System.out.println("entrance");
		if (session.getAttribute("logonSuccessYN") == "Y") {
			// registration.setRuleId(String.valueOf(session.getAttribute("sessNormalRegRuleId")));
			// System.out.println("regiRuleId " + registration.getRuleId());

			PointOfSale orderInfo = pointOfSaleService.getOrderInfo(pointOfSale);
			// redirectAttributes.addFlashAttribute("receiptInfo", receiptInfo);
			// System.out.println("receiptInfoSList "
			// +receiptInfoSList.getReceiptList());

			return orderInfo;
		} else {
			return null;
		}
	}
	

	@RequestMapping(value = "saveOrder", method = RequestMethod.POST)
	public ModelAndView saveOrder(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (session.getAttribute("logonSuccessYN") == "Y") {
			PointOfSale oPointOfSale = new PointOfSale();
			pointOfSale.setUpdateBy((String) session.getAttribute("employeeid"));

			oPointOfSale = pointOfSaleService.saveOrder(pointOfSale);

			// redirectAttributes.addFlashAttribute("registration",
			// registration);
			redirectAttributes.addFlashAttribute("message", oPointOfSale.getMessage());
			redirectAttributes.addFlashAttribute("mCode", oPointOfSale.getmCode());

			redirectAttributes.addFlashAttribute("pointOfSale", oPointOfSale);

			return new ModelAndView("redirect:/pointOfSale/orderManagement");
		} else {
			return new ModelAndView("redirect:/login");
		}
	}
	
	
	@RequestMapping(value = "discountView", method = RequestMethod.GET)
	public ModelAndView discountView(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			LookupModel lookupModel, final RedirectAttributes redirectAttributes) {
		
		if (session.getAttribute("logonSuccessYN") == "Y") {
			
			String menuId = "M0204";
			MenuInfo menuInfo = loginService.checkUserAuthorization((String) session.getAttribute("employeeid"),
					menuId);
			
			if (menuInfo.getMenuId().equals(menuId)) {
			
				ModelAndView mav = new ModelAndView();

			mav.setViewName("discountView");
			mav.addObject("employeeList", lookupService.employeeList(lookupModel));
			return mav;
			
			  } else {
					redirectAttributes.addFlashAttribute("message", "You are not authorized for this Page !");
					redirectAttributes.addFlashAttribute("mCode", "0000");
					return new ModelAndView("redirect:/");
				}
		} else {
			return new ModelAndView("login");

		}
	}
	

	@RequestMapping(value = "discount", method = RequestMethod.GET)
	public ModelAndView discount(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			LookupModel lookupModel, final RedirectAttributes redirectAttributes) {
		if (session.getAttribute("logonSuccessYN") == "Y") {
			
			String menuId = "M0204";
			MenuInfo menuInfo = loginService.checkUserAuthorization((String) session.getAttribute("employeeid"),
					menuId);
			
			if (menuInfo.getMenuId().equals(menuId)) {
			
			ModelAndView mav = new ModelAndView();

			mav.setViewName("discount");
			mav.addObject("employeeList", lookupService.employeeList(lookupModel));
			return mav;
			
			} else {
				redirectAttributes.addFlashAttribute("message", "You are not authorized for this Page !");
				redirectAttributes.addFlashAttribute("mCode", "0000");
				return new ModelAndView("redirect:/");
			}
		} else {
			return new ModelAndView("login");

		}
	}
	

	@RequestMapping(value = "getOrderPrice", method = RequestMethod.POST)
	public @ResponseBody PointOfSale getOrderPrice(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			final RedirectAttributes redirectAttributes) {
		
		
		if (session.getAttribute("logonSuccessYN") == "Y") {
			
			PointOfSale oPointOfSale = new PointOfSale();

			oPointOfSale = pointOfSaleService.getOrderPrice(pointOfSale);
			//System.out.println("price " + oPointOfSale.getItemPrice());
			return oPointOfSale;

		} else {
			return null;
		}

	}
	
	
	@RequestMapping(value = "saveDiscount", method = RequestMethod.POST)
	public ModelAndView saveDiscount(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			final RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (session.getAttribute("logonSuccessYN") == "Y") {
			try {
				pointOfSale.setUpdateBy((String) session.getAttribute("employeeid"));
				PointOfSale oPointOfSale = new PointOfSale();

				oPointOfSale = pointOfSaleService.saveDiscount(pointOfSale);

				redirectAttributes.addFlashAttribute("message", oPointOfSale.getMessage());
				redirectAttributes.addFlashAttribute("mCode", oPointOfSale.getmCode());

				redirectAttributes.addFlashAttribute("pointOfSale", pointOfSale);
				return new ModelAndView("redirect:/pointOfSale/discount");
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("mCode", "0000");
				redirectAttributes.addFlashAttribute("message", "Error occured!!");
				return new ModelAndView("redirect:/pointOfSale/discount");
			}
		} else {
			return new ModelAndView("redirect:/login");
		}
	}

	
	@RequestMapping(value = "getDiscountList", method = RequestMethod.POST)
	public ModelAndView getDiscountList(@ModelAttribute PointOfSale pointOfSale, LookupModel lookupModel,
			HttpSession session, final RedirectAttributes redirectAttributes) throws Exception {
		if (session.getAttribute("logonSuccessYN") == "Y") {
			List<PointOfSale> oDiscountList = pointOfSaleService.getDiscountList(pointOfSale);
			if (oDiscountList.size() > 0) {
				redirectAttributes.addFlashAttribute("discountList", oDiscountList);
				redirectAttributes.addFlashAttribute("discountListSize", oDiscountList.size());
				redirectAttributes.addFlashAttribute("pointOfSale", pointOfSale);
			} else {
				redirectAttributes.addFlashAttribute("discountListNotFound", "No Data Found!");
			}
			redirectAttributes.addFlashAttribute("pointOfSale", pointOfSale);
			return new ModelAndView("redirect:/pointOfSale/discountView");

		} else {
			return new ModelAndView("redirect:/login");
		}
	}
	
	
	@RequestMapping(value = "getDiscountInfo", method = RequestMethod.GET)
	public ModelAndView getDiscountInfo(@ModelAttribute PointOfSale pointOfSale, LookupModel lookupModel,
			HttpSession session, HttpServletRequest request, final RedirectAttributes redirectAttributes)
			throws Exception {
		if (session.getAttribute("logonSuccessYN") == "Y") {
			
			PointOfSale oPointOfSale = new PointOfSale();
			oPointOfSale = pointOfSaleService.getDiscountInfo(pointOfSale);
			redirectAttributes.addFlashAttribute("pointOfSale", oPointOfSale);
		}else {
			return new ModelAndView("redirect:/login");
		}

		return new ModelAndView("redirect:/pointOfSale/discount");
	}
	
	
	@RequestMapping(value = "getOrderInfoList", method = RequestMethod.POST)
	public ModelAndView getOrderInfoList(@ModelAttribute PointOfSale pointOfSale, LookupModel lookupModel,
			HttpSession session) throws Exception {
		
		if (session.getAttribute("logonSuccessYN") == "Y") {
			
			ModelAndView mav = new ModelAndView("orderInfoList");
			List<PointOfSale> oOrderInfoList = pointOfSaleService.getOrderInfoList(pointOfSale);
			if (oOrderInfoList.size() > 0) {
				mav.addObject("orderInfoList", oOrderInfoList);
			} else {
				mav.addObject("orderInfoListNotFound", "No Data Found!");
			}
			return mav;

		} else {
			return new ModelAndView("redirect:/login");
		}
	}
	
	
	@RequestMapping(value = "orderReprint", method = RequestMethod.GET)
	public ModelAndView orderReprint(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			LookupModel lookupModel) {
		
		if (session.getAttribute("logonSuccessYN") == "Y") {
			ModelAndView mav = new ModelAndView();

			mav.setViewName("orderReprint");
			mav.addObject("employeeList", lookupService.employeeList(lookupModel));
			return mav;

		} else {
			return new ModelAndView("login");

		}
	}
	
	
	@RequestMapping(value = "getOrderList", method = RequestMethod.POST)
	public ModelAndView getOrderList(@ModelAttribute PointOfSale pointOfSale, LookupModel lookupModel,
			HttpSession session, final RedirectAttributes redirectAttributes) throws Exception {
		if (session.getAttribute("logonSuccessYN") == "Y") {
			List<PointOfSale> oOrderList = pointOfSaleService.getOrderList(pointOfSale);
			if (oOrderList.size() > 0) {
				redirectAttributes.addFlashAttribute("orderList", oOrderList);
				redirectAttributes.addFlashAttribute("orderListSize", oOrderList.size());
				redirectAttributes.addFlashAttribute("pointOfSale", pointOfSale);
			} else {
				redirectAttributes.addFlashAttribute("orderListNotFound", "No Data Found!");
			}
			redirectAttributes.addFlashAttribute("pointOfSale", pointOfSale);
			return new ModelAndView("redirect:/pointOfSale/orderReprint");

		} else {
			return new ModelAndView("redirect:/login");
		}
	}
	
	
	@RequestMapping(value = "getPendingOrderInfoList", method = RequestMethod.POST)
	public ModelAndView getPendingOrderInfoList(@ModelAttribute PointOfSale pointOfSale, LookupModel lookupModel,
			HttpSession session) throws Exception {
		
		if (session.getAttribute("logonSuccessYN") == "Y") {
			
			ModelAndView mav = new ModelAndView("orderInfoList");
			List<PointOfSale> oPendingOrderInfoList = pointOfSaleService.getPendingOrderInfoList(pointOfSale);
			if (oPendingOrderInfoList.size() > 0) {
				mav.addObject("pendingOrderInfoList", oPendingOrderInfoList);
			} else {
				mav.addObject("pendingOrderInfoListNotFound", "No Data Found!");
			}
			return mav;

		} else {
			return new ModelAndView("redirect:/login");
		}
	}
	
	
	@RequestMapping(value = "cancelOrder", method = RequestMethod.GET)
	public ModelAndView cancelOrder(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			final RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (session.getAttribute("logonSuccessYN") == "Y") {
			System.out.println("entrance " + pointOfSale.getEncOrderId());
			try {
				pointOfSale.setUpdateBy((String) session.getAttribute("employeeid"));
				PointOfSale oPointOfSale = new PointOfSale();

				oPointOfSale = pointOfSaleService.cancelOrder(pointOfSale);

				redirectAttributes.addFlashAttribute("message", oPointOfSale.getMessage());
				redirectAttributes.addFlashAttribute("mCode", oPointOfSale.getmCode());

				redirectAttributes.addFlashAttribute("pointOfSale", pointOfSale);
				return new ModelAndView("redirect:/pointOfSale/orderManagement");
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("mCode", "0000");
				redirectAttributes.addFlashAttribute("message", "Error occured!!");
				return new ModelAndView("redirect:/pointOfSale/orderManagement");
			}
		} else {
			return new ModelAndView("redirect:/login");
		}
	}
	
	@RequestMapping(value = "getOrderEditList", method = RequestMethod.GET)
	public ModelAndView getOrderEditList(@ModelAttribute PointOfSale pointOfSale, LookupModel lookupModel,
			HttpSession session, HttpServletRequest request, final RedirectAttributes redirectAttributes) throws Exception {
		
		if (session.getAttribute("logonSuccessYN") == "Y") {
			//ModelAndView mav = new ModelAndView("orderInfoList");
			List<PointOfSale> oOrderEditList = pointOfSaleService.getOrderEditList(pointOfSale);
			System.out.println("list size " + oOrderEditList.size());
			if (oOrderEditList.size() > 0) {
				//mav.addObject("orderEditList", oOrderEditList);
				redirectAttributes.addFlashAttribute("orderInfoList", oOrderEditList);
				redirectAttributes.addFlashAttribute("orderInfoListIndex", oOrderEditList.size() - 1);
			} else {
				//mav.addObject("orderEditListNotFound", "No Data Found!");
				redirectAttributes.addFlashAttribute("orderEditListNotFound", "No Data Found!");
			}
			redirectAttributes.addFlashAttribute("pointOfSale", pointOfSale);
			return new ModelAndView("redirect:/pointOfSale/orderManagement");

		} else {
			return new ModelAndView("redirect:/login");
		}
	}
	
	
	@RequestMapping(value = "getOrderTotalAmount", method = RequestMethod.POST)
	public @ResponseBody PointOfSale getOrderTotalAmount(HttpSession session, PointOfSale pointOfSale,
			final RedirectAttributes redirectAttribute) throws Exception {
		if (session.getAttribute("logonSuccessYN") == "Y") {
			PointOfSale oPointOfSale = new PointOfSale();
			oPointOfSale = pointOfSaleService.getOrderTotalAmount(pointOfSale.getEncOrderId());
			return oPointOfSale;
		} else {
			return new PointOfSale();
		}
	}
	
	@RequestMapping(value = "saveOrderFinalize", method = RequestMethod.POST)
	public ModelAndView saveOrderFinalize(@ModelAttribute PointOfSale pointOfSale, HttpSession session,
			final RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (session.getAttribute("logonSuccessYN") == "Y") {
			try {
				pointOfSale.setUpdateBy((String) session.getAttribute("employeeid"));
				PointOfSale oPointOfSale = new PointOfSale();

				oPointOfSale = pointOfSaleService.saveOrderFinalize(pointOfSale);

				redirectAttributes.addFlashAttribute("message", oPointOfSale.getMessage());
				redirectAttributes.addFlashAttribute("mCode", oPointOfSale.getmCode());

				redirectAttributes.addFlashAttribute("pointOfSale", pointOfSale);
				return new ModelAndView("redirect:/pointOfSale/orderManagement");
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("mCode", "0000");
				redirectAttributes.addFlashAttribute("message", "Error occured!!");
				return new ModelAndView("redirect:/pointOfSale/orderManagement");
			}
		} else {
			return new ModelAndView("redirect:/login");
		}
	}
	
/*	@RequestMapping(value = "getOrderTotalAmount", method = RequestMethod.POST)
	public ModelAndView getOrderTotalAmount(@ModelAttribute PointOfSale pointOfSale, LookupModel lookupModel,
			HttpSession session, final RedirectAttributes redirectAttributes) throws Exception {
		
		if (session.getAttribute("logonSuccessYN") == "Y") {
			PointOfSale oPointOfSale = new PointOfSale();
			oPointOfSale = pointOfSaleService.getOrderTotalAmount(pointOfSale.getEncOrderId());
			redirectAttributes.addFlashAttribute("pointOfSale", oPointOfSale);
			return new ModelAndView("redirect:/pointOfSale/orderReprint");

		} else {
			return new ModelAndView("redirect:/login");
		}
	}*/
	
}












