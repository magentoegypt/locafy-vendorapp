//
//  ced_faqFilterView.swift
//  VenderApp
//
//  Created by Cedcoss on 24/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_faqFilterView: UIView {

    @IBOutlet weak var idField: UITextField!
    @IBOutlet weak var prodIdField: UITextField!
    @IBOutlet weak var titleField: UITextField!
    @IBOutlet weak var customerEmailField: UITextField!
    @IBOutlet weak var publishFrom: UITextField!
    @IBOutlet weak var publishTo: UITextField!
    @IBOutlet weak var statusBtn: UIButton!
    @IBOutlet weak var applyFilterBtn: UIButton!
    @IBOutlet weak var resetFilterBtn: UIButton!
    
    @IBOutlet weak var idLbl: UILabel!
    @IBOutlet weak var prodIdLbl: UILabel!
    @IBOutlet weak var prodTitleLvl: UILabel!
    @IBOutlet weak var emailLbl: UILabel!
    @IBOutlet weak var createdLbl: UILabel!
    @IBOutlet weak var statusLbl: UILabel!
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
        statusBtn.addTarget(self, action: #selector(dropdownBtnTapped(_:)), for: .touchUpInside)
       
        resetFilterBtn.setTitle("RESET FILTER".localized, for: .normal)
        applyFilterBtn.setTitle("FILTER".localized, for: .normal)
        publishFrom.placeholder = "From".localized
        publishTo.placeholder = "To".localized
        
        idLbl.text = "FAQ ID".localized
        statusBtn.setTitle("Please Select Option".localized, for: .normal)
        prodIdLbl.text = "Product ID".localized
        prodTitleLvl.text = "Title".localized
        emailLbl.text = "Customer Email".localized
        createdLbl.text = "Created At".localized
        statusLbl.text = "Status".localized
        
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
       
        
    }
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ced_faqFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
    @objc func dropdownBtnTapped(_ sender:UIButton){
        print("print here")
        let dropDown = DropDown();
        dropDown.dataSource = ["Enabled".localized,"Disabled".localized];
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal);
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)

        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    @objc func applyFilterTapped(_ sender:UIButton){
        var postData = [String:Any]()
        postData["id"] = idField.text
        postData["email_id"] = customerEmailField.text
        postData["title"] = titleField.text
        postData["product_id"] = prodIdField.text
        postData["publish_date"] = ["from":publishFrom.text,"to":publishTo.text]
        if statusBtn.currentTitle == "Enabled"{
            postData["is_active"] = "1"
        }else if statusBtn.currentTitle == "Disabled"{
            postData["is_active"] = "0"
        }else{
            postData["is_active"] = ""
        }
        self.didSelectFilter?(postData.convtToJson() as String)
    }
    
    @objc func resetFilterTapped(_ sender:UIButton){
        idField.text = ""
        customerEmailField.text = ""
        titleField.text = ""
        prodIdField.text = ""
        publishTo.text = ""
        publishFrom.text = ""
        statusBtn.setTitle("Please Select Option".localized, for: .normal)
        self.didSelectFilter?("")
    }

    
}
