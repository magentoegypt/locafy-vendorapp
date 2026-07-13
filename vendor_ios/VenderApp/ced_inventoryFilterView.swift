//
//  ced_inventoryFilterView.swift
//  VenderApp
//
//  Created by Cedcoss on 30/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_inventoryFilterView: UIView {
    
    @IBOutlet weak var prodIdField: UITextField!
    @IBOutlet weak var prodNamwField: UITextField!
    @IBOutlet weak var prodQtyField: UITextField!
    @IBOutlet weak var prodTypeField: UITextField!
    @IBOutlet weak var priceFromField: UITextField!
    @IBOutlet weak var priceToField: UITextField!
    @IBOutlet weak var applyFilterBtn: UIButton!
    @IBOutlet weak var resetFilterBtn: UIButton!
    
    @IBOutlet weak var prodIdLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var qtyLbl: UILabel!
    @IBOutlet weak var typeLbl: UILabel!
    @IBOutlet weak var priceLbl: UILabel!
    var didSelectFilter: ( (String) -> Void)?
    
    var view:UIView!
    
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
       // self.view.tag=1998
        // use bounds not frame or it'll be offset
        view.frame = bounds
        
        // Make the view stretch with containing view
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleWidth, UIView.AutoresizingMask.flexibleHeight]
        
        applyFilterBtn.backgroundColor = DynamicColor.themeColor
        resetFilterBtn.backgroundColor = DynamicColor.themeColor
        applyFilterBtn.addTarget(self, action: #selector(applyFilterTapped(_:)), for: .touchUpInside)
        resetFilterBtn.addTarget(self, action: #selector(resetFilterTapped(_:)), for: .touchUpInside)
        
        prodIdField.keyboardType = .numberPad
        prodQtyField.keyboardType = .numberPad
        resetFilterBtn.setTitle("RESET FILTER".localized, for: .normal)
        applyFilterBtn.setTitle("Apply".localized, for: .normal)
        prodIdLbl.text = "Product ID".localized
        nameLbl.text = "Product Name".localized
        qtyLbl.text = "Quantity".localized
        typeLbl.text = "Type".localized
        priceLbl.text = "Price".localized
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
       
        
    }
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ced_inventoryFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
    @objc func applyFilterTapped(_ sender:UIButton){
        var postData = [String:Any]()
        postData["name"] = prodNamwField.text
        postData["type"] = prodTypeField.text
        postData["qty"] = prodQtyField.text
        postData["product_id"] = prodIdField.text
        postData["price"] = ["from":priceFromField.text,"to":priceToField.text]
        
        if prodNamwField.text == "" && prodTypeField.text == "" && prodQtyField.text == "" && prodIdField.text == "" && priceToField.text == "" && priceFromField.text == ""{
            self.didSelectFilter?("")
            return
        }
        self.didSelectFilter?(postData.convtToJson() as String)
        
    }
    
    @objc func resetFilterTapped(_ sender:UIButton){
        prodNamwField.text = ""
        prodTypeField.text = ""
        prodQtyField.text = ""
        prodIdField.text = ""
        priceToField.text = ""
        priceFromField.text = ""
        self.didSelectFilter?("")
    }
    
}
