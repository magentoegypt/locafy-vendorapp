//
//  RmaFilterXibs.swift
//  VenderApp
//
//  Created by Macmini on 09/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class RmaFilterXibs: UIView {

    
    @IBOutlet weak var Filter: UIButton!
    
    @IBOutlet weak var resetfilter: UIButton!
    
    @IBOutlet weak var custumeremail: UITextField!
    
    @IBOutlet weak var updateto: UITextField!
    @IBOutlet weak var custumername: UITextField!
    
    
    @IBOutlet weak var SelectStatus: UIButton!
    
    @IBOutlet weak var updatedfrom: UITextField!
    @IBOutlet weak var orderid: UITextField!
    
    @IBOutlet weak var rmaid: UITextField!
    
    
    @IBOutlet weak var resolutionRequested: UITextField!
    
    @IBOutlet weak var orderIdLbl: UILabel!
    @IBOutlet weak var rmaIdLbl: UILabel!
    @IBOutlet weak var customerNameLbl: UILabel!
    @IBOutlet weak var customerEmailLbl: UILabel!
    @IBOutlet weak var updatedAtLbl: UILabel!
    @IBOutlet weak var statusLbl: UILabel!
    @IBOutlet weak var resoltionLbl: UILabel!
    var view: UIView!
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
        
        orderIdLbl.text = "Order Id".localized
        rmaIdLbl.text = "RMA Id".localized
        customerNameLbl.text = "Customer Name".localized
        customerEmailLbl.text = "Customer Email".localized
        updateto.placeholder = "To".localized
        updatedfrom.placeholder = "From".localized
        updatedAtLbl.text = "Updated At".localized
        statusLbl.text = "Status".localized
        SelectStatus.setTitle("Select Status".localized, for: .normal)
        resolutionRequested.text = "Resolution Requested".localized
        Filter.setTitle("Filter".localized, for: .normal)
        resetfilter.setTitle("Reset".localized, for: .normal)
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "RmaFilterXibs", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        Filter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        resetfilter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }

}
