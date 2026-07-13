//
//  ced_outofstockFilter.swift
//  VenderApp
//
//  Created by Cedcoss on 26/07/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_outofstockFilter: UIView {
    
    @IBOutlet weak var headingLabel: UIButton!
    @IBOutlet weak var applyFilter: UIButton!
    @IBOutlet weak var clearButton: UIButton!
    
    @IBOutlet weak var skuLbl:UILabel!
    @IBOutlet weak var prodNameLbl:UILabel!
    @IBOutlet weak var typeLbl:UILabel!
    @IBOutlet weak var qtyLbl:UILabel!
    
    @IBOutlet weak var skuField:UITextField!
    @IBOutlet weak var nameField:UITextField!
    @IBOutlet weak var typeField:UITextField!
    @IBOutlet weak var fromField:UITextField!
    @IBOutlet weak var toField:UITextField!

    var didSelectFilter: ( (String) -> Void)?
    
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
        applyFilter.setThemeColor()
        clearButton.setThemeColor()
        
        skuLbl.text = "SKU".localized
        prodNameLbl.text = "Product Name".localized
        qtyLbl.text = "Quantity".localized
        typeLbl.text = "Product Type".localized
        clearButton.setTitle("Reset".localized, for: .normal)
        applyFilter.setTitle("Apply".localized, for: .normal)
        fromField.placeholder = "From".localized
        toField.placeholder = "To".localized
        applyFilter.addTarget(self, action: #selector(applyFilterTapped(_:)), for: .touchUpInside)
        clearButton.addTarget(self, action: #selector(resetFilterTapped(_:)), for: .touchUpInside)
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ced_outofstockFilter", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        applyFilter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        clearButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }
    
    @objc func resetFilterTapped(_ sender:UIButton){
        fromField.placeholder = "From".localized
        toField.placeholder = "To".localized
        fromField.text = ""
        toField.text = ""
        skuField.text = ""
        nameField.text = ""
        typeField.text = ""
        self.didSelectFilter?("")
    }
    @objc func applyFilterTapped(_ sender:UIButton){
        let sku = skuField.text ?? ""
        let name = nameField.text ?? ""
        let type = typeField.text ?? ""
        let from = fromField.text ?? ""
        let to = toField.text ?? ""
        let poststring = ["sku":sku,"name":name,"type":type,"qty":["from":from,"to":to]].convtToJson() as String
        self.didSelectFilter?(poststring)
    }

}
