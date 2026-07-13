//
//  SalesReportFilterView.swift
//  VenderApp
//
//  Created by cedcoss on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class SalesReportFilterView: UIView {

    @IBOutlet weak var headingLabel: UIButton!
    @IBOutlet weak var deliveryStatus: UIButton!
    
    @IBOutlet weak var products: UITextField!
    
    @IBOutlet weak var sellingPriceFrom: UITextField!
    @IBOutlet weak var sellingPriceTo: UITextField!
    
    @IBOutlet weak var courierName: UITextField!
    
    @IBOutlet weak var trackingNumber: UITextField!
    
    @IBOutlet weak var orderDateFrom: UITextField!
    @IBOutlet weak var courierLbl: UILabel!
    @IBOutlet weak var trackingLbl: UILabel!
    
    @IBOutlet weak var orderDateTo: UITextField!
    
    @IBOutlet weak var applyFilter: UIButton!
    
    @IBOutlet weak var clearButton: UIButton!
    
    @IBOutlet weak var delStatusLbl: UILabel!
    @IBOutlet weak var prodLbl: UILabel!
    @IBOutlet weak var sellPricelbl: UILabel!
    @IBOutlet weak var orderdateLbl: UILabel!
    
    var didSelectFilter: ( (String) -> Void)?
    var orderStatusData = [String:String]()
    
    var view: UIView!;
    override init(frame: CGRect)
    {
        // 1. setup any properties here
        
        // 2. call super.init(frame:)
        super.init(frame: frame)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    required init?(coder aDecoder: NSCoder)
    {
        // 1. setup any properties here
        
        // 2. call super.init(coder:)
        super.init(coder: aDecoder)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    @objc func xibSetup()
    {
        view = loadViewFromNib()
        
        // use bounds not frame or it'll be offset
        view.frame = bounds
        
        // Make the view stretch with containing view
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleWidth, UIView.AutoresizingMask.flexibleHeight]
        headingLabel.setThemeColor()
        headingLabel.setTitle("Vendor App".localized, for: .normal)
        applyFilter.addTarget(self, action: #selector(applyFilterTapped(_:)), for: .touchUpInside)
        clearButton.addTarget(self, action: #selector(resetFilterTapped(_:)), for: .touchUpInside)
        applyFilter.setThemeColor()
        clearButton.setThemeColor()
        applyFilter.setTitle("Apply".localized, for: .normal)
        clearButton.setTitle("Reset".localized, for: .normal)
        deliveryStatus.layer.borderColor = UIColor.black.cgColor
        deliveryStatus.layer.borderWidth = 0.5;
        courierLbl.text = "Courier Name".localized
        trackingLbl.text = "Tracking Number".localized
        delStatusLbl.text = "Order Payment Status".localized
        prodLbl.text = "Products".localized
        sellPricelbl.text = "Selling Price".localized
        orderdateLbl.text = "Order Date".localized
        orderDateFrom.placeholder = "From".localized
        orderDateTo.placeholder = "To".localized
        sellingPriceTo.placeholder = "To".localized
        sellingPriceFrom.placeholder = "From".localized
        deliveryStatus.setTitle("Select".localized, for: .normal)
        deliveryStatus.addTarget(self, action: #selector(deliveryStatusClicked(_:)), for: .touchUpInside)
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "SalesReportFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        applyFilter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        clearButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }

    @objc func resetFilterTapped(_ sender:UIButton){
        orderDateFrom.placeholder = "From".localized
        orderDateTo.placeholder = "To".localized
        sellingPriceTo.placeholder = "To".localized
        sellingPriceFrom.placeholder = "From".localized
        sellingPriceTo.text = ""
        sellingPriceFrom.text = ""
        orderDateFrom.text = ""
        orderDateTo.text = ""
        products.text = ""
        courierName.text = ""
        trackingNumber.text = ""
        deliveryStatus.setTitle("Select".localized, for: .normal)
        self.didSelectFilter?("")
    }
    
    @objc func applyFilterTapped(_ sender:UIButton){
        let datefrom = orderDateFrom.text ?? ""
        let dateto = orderDateTo.text ?? ""
        let prod = products.text ?? ""
        let sellfrom = sellingPriceFrom.text ?? ""
        let sellto = sellingPriceTo.text ?? ""
        let orderStatus = orderStatusData.someKey(forValue: deliveryStatus.currentTitle ?? "") ?? ""
        let courier = courierName.text ?? ""
        let tracking = trackingNumber.text ?? ""
        let poststring = ["created_at":["from":datefrom,"to":dateto],"product_qty":prod,"base_order_total":["from":sellfrom,"to":sellto],"order_payment_state":orderStatus,"courier_name":courier,"tracking_number":tracking].convtToJson() as String
        self.didSelectFilter?(poststring)
    }
    
    @objc func deliveryStatusClicked(_ sender: UIButton)
    {
        let dropDown = DropDown()
        dropDown.dataSource = Array(orderStatusData.values)
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            _ = dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
}
