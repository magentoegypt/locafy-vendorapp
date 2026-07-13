//
//  QuotationListingFilterView.swift
//  VenderApp
//
//  Created by cedcoss on 16/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class QuotationListingFilterView: UIView {

    @IBOutlet weak var headingLabel: UIButton!
    
    @IBOutlet weak var idLabel: UITextField!
    
    
    @IBOutlet weak var emailLabel: UITextField!
    
    @IBOutlet weak var productNameLabel: UITextField!
    
    @IBOutlet weak var requestedQtyLabel: UITextField!
    
    @IBOutlet weak var status: UIButton!
    
    @IBOutlet weak var estimatedBudgetLabel: UITextField!
    
    @IBOutlet weak var filterButton: UIButton!
    
    @IBOutlet weak var clearButton: UIButton!
    
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
        headingLabel.backgroundColor = DynamicColor.secondaryColor//setThemeColor()
        headingLabel.setTitle("Filter", for: .normal)
        filterButton.setThemeColor()
        clearButton.setThemeColor()
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "QuotationListingFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        filterButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        clearButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }

}
