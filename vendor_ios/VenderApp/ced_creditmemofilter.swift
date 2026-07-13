//
//  ced_creditmemofilter.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 01/07/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_creditmemofilter: UIView,UITextFieldDelegate {
    var view:UIView!
    
    @IBOutlet weak var creditmemoId: UITextField!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var creditDateTo: UITextField!
    @IBOutlet weak var creditDateFrom: UITextField!
    @IBOutlet weak var orderId: UITextField!
    @IBOutlet weak var orderDateFrom: UITextField!
    @IBOutlet weak var orderDateTo: UITextField!
    @IBOutlet weak var billingName: UITextField!
    @IBOutlet weak var status: UIButton!
    @IBOutlet weak var amountFrom: UITextField!
    @IBOutlet weak var amountTo: UITextField!
    
    @IBOutlet weak var ResetFilter: UIButton!
    @IBOutlet weak var filter: UIButton!
    
    @IBOutlet weak var t1: UILabel!
    @IBOutlet weak var t2: UILabel!
    @IBOutlet weak var t3: UILabel!
    @IBOutlet weak var t4: UILabel!
    @IBOutlet weak var t5: UILabel!
    @IBOutlet weak var t6: UILabel!
    @IBOutlet weak var t7: UILabel!
    
    
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
       // topLabel.backgroundColor = color
        filter.backgroundColor = color
        ResetFilter.backgroundColor = color
        view.layer.borderColor = UIColor.black.cgColor
        view.layer.borderWidth = 0.2

        t1.text = "Credit Memo #".localized
        t2.text = "Created At".localized
        t3.text = "Order #".localized
        t4.text = "Order Date".localized
        t5.text = "Bill To Name".localized
        t6.text = "Status".localized
        t7.text = "Refunded".localized
        filter.setTitle("Filter".localized, for: .normal)
        ResetFilter.setTitle("Reset Filter".localized, for: .normal)
        status.setTitle("Select".localized, for: .normal)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "creditMemoFilter", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
}
