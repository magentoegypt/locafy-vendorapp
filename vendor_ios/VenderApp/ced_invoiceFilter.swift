//
//  ced_invoiceFilter.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 09/07/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_invoiceFilter: UIView,UITextFieldDelegate {

    @IBOutlet weak var inID: UILabel!
    @IBOutlet weak var indate: UILabel!
    @IBOutlet weak var inorder: UILabel!
    @IBOutlet weak var indate2: UILabel!
    @IBOutlet weak var inBill: UILabel!
    @IBOutlet weak var oiStatus: UILabel!
    @IBOutlet weak var inAmount: UILabel!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var invoiceId: UITextField!
    @IBOutlet weak var invoiceDateTo: UITextField!
    @IBOutlet weak var invoiceDateFrom: UITextField!
    @IBOutlet weak var orderId: UITextField!
    @IBOutlet weak var orderDateFrom: UITextField!
    @IBOutlet weak var orderDateTo: UITextField!
    @IBOutlet weak var billingName: UITextField!
    @IBOutlet weak var status: UIButton!
    @IBOutlet weak var amountFrom: UITextField!
    @IBOutlet weak var amountTo: UITextField!
    
    @IBOutlet weak var ResetFilter: UIButton!
    @IBOutlet weak var filter: UIButton!
    var view:UIView!
    
    var statusValue = ""
    
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
        topLabel.text="Vendor App".localized
        filter.setTitle("Filter".localized, for:   .normal)
        ResetFilter.setTitle("Reset Filter".localized, for: .normal)
        inID.text = " Invoice Id#".localized
        indate.text=" Invoice Date".localized
        inorder.text=" Order #".localized
        indate2.text=" Order Date".localized
        inBill.text=" Bill to name".localized
        oiStatus.text=" Status".localized
        inAmount.text=" Amount".localized
        invoiceDateTo.placeholder="To".localized
        invoiceDateFrom.placeholder="From".localized
        orderDateFrom.placeholder="From".localized
        orderDateTo.placeholder="To".localized
        status.setTitle("Select".localized, for: .normal)
        amountTo.placeholder="To".localized
        amountFrom.placeholder="From".localized
        filter.backgroundColor = color
        ResetFilter.backgroundColor = color
        view.layer.borderColor = UIColor.black.cgColor
        view.layer.borderWidth = 0.2
//        orderDateFrom.delegate = self
//        orderDateTo.delegate = self
//        orderDateFrom.tag = 7776;
//        orderDateTo.tag = 7777;
//        invoiceDateFrom.delegate = self
//        invoiceDateTo.delegate   = self
//        invoiceDateFrom.tag = 7774;
//        invoiceDateTo.tag = 7775;
        status.addTarget(self, action: #selector(ced_invoiceFilter.checkStatus(_:)), for: UIControl.Event.touchUpInside)
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "invoiceFilter", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
  
    @objc func checkStatus(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = ["Pending_status".localized,"Paid".localized,"Canceled".localized];
        let dataSourceValue = ["Pending","Paid","Canceled"];
        
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.statusValue = dataSourceValue[index]
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
 
    }

}
