//
//  ced_PO_Filter.swift
//  VenderApp
//
//  Created by Macmini on 17/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_PO_Filter: UIView {

    var view: UIView!
    
    @IBOutlet weak var POIncrementId: UITextField!
  
    @IBOutlet weak var Status: UIButton!
    
    @IBOutlet weak var CreatedAtFrom: UITextField!
    @IBOutlet weak var createdAtTo: UITextField!
    @IBOutlet weak var quoteID: UITextField!
    @IBOutlet weak var Filter: UIButton!
    @IBOutlet weak var ResetFilter: UIButton!
    
    @IBOutlet weak var poIncIdLbl: UILabel!
    @IBOutlet weak var quoteIdLbl: UILabel!
    @IBOutlet weak var createdAtLbl: UILabel!
    @IBOutlet weak var statusLbl: UILabel!
    
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
        
        poIncIdLbl.text = "PO Increment Id".localized
        quoteIdLbl.text = "Quote Id".localized
        createdAtLbl.text = "Created At".localized
        statusLbl.text = "Status".localized
        Status.setTitle("Select Status".localized, for: .normal)
        createdAtTo.placeholder = "To".localized
        CreatedAtFrom.placeholder = "From".localized
        
        addSubview(view)
    }
    
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ced_PO_Filter", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        ResetFilter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        Filter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }
    
    
}
