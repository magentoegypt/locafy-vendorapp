//
//  storeFilter.swift
//  VenderApp
//
//  Created by cedcoss on 25/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class storeFilter: UIView {
    @IBOutlet weak var productIdHead: UILabel!
    
    @IBOutlet weak var productNameHead: UILabel!
    
    @IBOutlet weak var emailHeight: NSLayoutConstraint!
    
    @IBOutlet weak var productPriceHead: UILabel!
    
    @IBOutlet weak var productQtyHead: UILabel!
    
    @IBOutlet weak var statusHeight: NSLayoutConstraint!
    
    @IBOutlet weak var productStatusHead: UILabel!
    
    @IBOutlet weak var productId: UITextField!
    
    @IBOutlet weak var id: UILabel!
    @IBOutlet weak var productName: UITextField!
    
    
    @IBOutlet weak var creation: UILabel!
    @IBOutlet weak var Name: UILabel!
    @IBOutlet weak var productPrice: UITextField!
    
    
    @IBOutlet weak var productQty: UITextField!
    
    @IBOutlet weak var productStatus: UIButton!
    
    @IBOutlet weak var filterButton: UIButton!
    
    @IBOutlet weak var resetButton: UIButton!
    
    
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
        
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "storeFilter", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        filterButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        resetButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }

}
