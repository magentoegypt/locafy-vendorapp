//
//  DealListFilterView.swift
//  VenderApp
//
//  Created by cedcoss on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class DealListFilterView: UIView {

    @IBOutlet weak var headingLabel: UIButton!
    @IBOutlet weak var dealIdHead: UILabel!
    
    @IBOutlet weak var productNameHead: UILabel!
    
    @IBOutlet weak var dealPriceHead: UILabel!
    
    @IBOutlet weak var dealEndHead: UILabel!
    
    @IBOutlet weak var dealStatusHead: UILabel!
    
    @IBOutlet weak var adminStatusHead: UILabel!
    
    @IBOutlet weak var dealId: UITextField!
    
    @IBOutlet weak var productName: UITextField!
    
    @IBOutlet weak var dealPriceFrom: UITextField!
    
    @IBOutlet weak var dealPriceTo: UITextField!
    
    @IBOutlet weak var dealEndFrom: UITextField!
    
    @IBOutlet weak var dealEndTo: UITextField!
    
    @IBOutlet weak var dealStatus: UIButton!
    
    @IBOutlet weak var adminStatus: UIButton!
    
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
        
        dealIdHead.text = "Deal Id".localized
        productNameHead.text = "Product name".localized
        dealPriceHead.text = "Deal Price".localized
        dealPriceTo.placeholder = "To".localized
        dealPriceFrom.placeholder = "From".localized
        dealEndHead.text = "Deal End".localized
        dealEndTo.text = "To".localized
        dealEndFrom.text = "From".localized
        dealStatusHead.text = "Deal Status".localized
        dealStatus.setTitle("-----Select-----".localized, for: .normal)
        adminStatusHead.text = "Admin Status".localized
        adminStatus.setTitle("-----Select-----".localized, for: .normal)
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "DealListFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        filterButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        resetButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }

}
