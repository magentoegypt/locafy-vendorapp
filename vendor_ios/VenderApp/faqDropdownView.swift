//
//  faqDropdownView.swift
//  VenderApp
//
//  Created by Cedcoss on 25/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class faqDropdownView: UIView {
    
    @IBOutlet weak var dropdownLabel: UILabel!
    @IBOutlet weak var dropdownBtn: UIButton!
    
//    var dropdown = DropDown()
    var fieldName = ""
    var fieldValue = ""
    var dropdownDatasource = [String:String]()
    
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
        dropdownBtn.addTarget(self, action: #selector(dropdownBtnTapped(_:)), for: .touchUpInside)
        dropdownBtn.setTitle("Please Select Option".localized, for: .normal)
       
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
       
        
    }
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "faqDropdownView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
    @objc func dropdownBtnTapped(_ sender:UIButton){
        print("print here")
        let dropDown = DropDown();
        dropDown.dataSource = Array(dropdownDatasource.keys);
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal);
            self.fieldValue = self.dropdownDatasource[item] ?? ""
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)

        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
}
