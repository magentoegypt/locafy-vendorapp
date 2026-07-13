//
//  ced_shipmentFilterView.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 09/07/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_shipmentFilterView: UIView,UITextFieldDelegate {

    @IBOutlet weak var t6: UILabel!
    @IBOutlet weak var t5: UILabel!
    @IBOutlet weak var t4: UILabel!
    @IBOutlet weak var t3: UILabel!
    @IBOutlet weak var t2: UILabel!
    @IBOutlet weak var t1: UILabel!
    @IBOutlet weak var shipmentQtyField: UITextField!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var shipmentId: UITextField!
    @IBOutlet weak var dateShippedFrom: UITextField!
    @IBOutlet weak var dateShippedTo: UITextField!
    @IBOutlet weak var orderId: UITextField!
    @IBOutlet weak var orderDatefrom: UITextField!
    @IBOutlet weak var orderDateTo: UITextField!
    @IBOutlet weak var shippingName: UITextField!
    
    @IBOutlet weak var filter: UIButton!    
    @IBOutlet weak var resetFilter: UIButton!
    var view:UIView!
    override init(frame: CGRect) {
        // 1. setup any properties here
        
        // 2. call super.init(frame:)
        super.init(frame: frame)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    required init?(coder aDecoder: NSCoder) {
        // 1. setup any properties here
        
        // 2. call super.init(coder:)
        super.init(coder: aDecoder)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    @objc func xibSetup() {
        view = loadViewFromNib()
        
        // use bounds not frame or it'll be offset
        view.frame = bounds
        
        // Make the view stretch with containing view
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleWidth, UIView.AutoresizingMask.flexibleHeight]
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
      //  topLabel.backgroundColor = color
        filter.backgroundColor = color
        resetFilter.backgroundColor = color
        view.layer.borderColor = UIColor.black.cgColor
        view.layer.borderWidth = 0.2
//        orderDateTo.delegate = self
//        orderDatefrom.delegate = self
//        dateShippedFrom.delegate = self
//        dateShippedTo.delegate = self
//        dateShippedFrom.tag = 7776
//        dateShippedTo.tag = 7777
//        orderDatefrom.tag = 7774
//        orderDateTo.tag = 7775
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        dateShippedTo.placeholder="To".localized
        dateShippedFrom.placeholder="From".localized
        orderDatefrom.placeholder="From".localized
        orderDateTo.placeholder="To".localized
        topLabel.text="Vendor App".localized
        t1.text=" Shipment #".localized
        t2.text=" Date Shipped".localized
        t3.text=" Order #".localized
        t4.text=" Order Date".localized
        t5.text=" Ship to name".localized
        t6.text=" Shipment Qty".localized
        filter.setTitle("Filter".localized, for: .normal)
        resetFilter.setTitle("Reset Filter".localized, for: .normal)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "shipmentFilter", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
}
