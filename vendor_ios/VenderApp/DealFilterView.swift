//
//  DealFilterView.swift
//  VenderApp
//
//  Created by cedcoss on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class DealFilterView: UIView {
    
    @IBOutlet weak var headingLabel: UIButton!
    
    
    @IBOutlet weak var productIdHead: UILabel!
    
    @IBOutlet weak var productNameHead: UILabel!
    
    @IBOutlet weak var productTypeHead: UILabel!
    
    @IBOutlet weak var productPriceHead: UILabel!
    
    @IBOutlet weak var productQtyHead: UILabel!
    
    @IBOutlet weak var productStatusHead: UILabel!
    
    @IBOutlet weak var productId: UITextField!
    
    @IBOutlet weak var productName: UITextField!
    
    @IBOutlet weak var productType: UIButton!
    
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
        
        productIdHead.text = "Product ID".localized
        productNameHead.text = "Product Name".localized
        productTypeHead.text = "Product Type".localized
        productType.setTitle("-----Select-----".localized, for: .normal)
        productPriceHead.text = "Price".localized
        productQtyHead.text = "Qty".localized
        productStatusHead.text = "Status".localized
        productStatus.setTitle("-----Select-----", for: .normal)
        filterButton.setTitle("Filter".localized, for: .normal)
        resetButton.setTitle("Reset".localized, for: .normal)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "DealFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        filterButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        resetButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }
}
