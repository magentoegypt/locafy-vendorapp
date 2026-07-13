//
//  ProductFilterView.swift
//  VenderApp
//
//  Created by cedcoss on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ProductFilterView: UIView {

    @IBOutlet weak var headingLabel: UIButton!
    @IBOutlet weak var applyFilter: UIButton!
    @IBOutlet weak var clearButton: UIButton!
    
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var skuLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var typeLbl: UILabel!
    @IBOutlet weak var viewsLbl: UILabel!
    
    @IBOutlet weak var DateFrom: UITextField!
    @IBOutlet weak var DateTo: UITextField!
    @IBOutlet weak var skuField: UITextField!
    @IBOutlet weak var nameField: UITextField!
    @IBOutlet weak var typeField: UITextField!
    @IBOutlet weak var viewsField: UITextField!
    
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
        applyFilter.addTarget(self, action: #selector(applyFilterTapped(_:)), for: .touchUpInside)
        clearButton.addTarget(self, action: #selector(resetFilterTapped(_:)), for: .touchUpInside)
        applyFilter.setThemeColor()
        clearButton.setThemeColor()
        applyFilter.setTitle("Apply".localized, for: .normal)
        clearButton.setTitle("Reset".localized, for: .normal)
        dateLabel.text = "Date".localized
        skuLbl.text = "SKU".localized
        nameLbl.text = "Product Name".localized
        typeLbl.text = "Product Type".localized
        viewsLbl.text = "Views".localized
        
        DateFrom.placeholder = "From".localized
        DateTo.placeholder = "To".localized
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ProductFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        applyFilter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        clearButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }

    @objc func resetFilterTapped(_ sender:UIButton){
        DateFrom.placeholder = "From".localized
        DateTo.placeholder = "To".localized
        DateFrom.text = ""
        DateTo.text = ""
        skuField.text = ""
        nameField.text = ""
        typeField.text = ""
        viewsField.text = ""
        self.didSelectFilter?("")
    }
    @objc func applyFilterTapped(_ sender:UIButton){
        let sku = skuField.text ?? ""
        let name = nameField.text ?? ""
        let type = typeField.text ?? ""
        let from = DateFrom.text ?? ""
        let to = DateTo.text ?? ""
        let views = viewsField.text ?? ""
        let poststring = ["sku":sku,"name":name,"type_id":type,"views":views,"date_search":["from":from,"to":to]].convtToJson() as String
        self.didSelectFilter?(poststring)
    }
    
}
