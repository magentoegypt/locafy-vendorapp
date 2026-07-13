//
//  ced_AddTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_AddTableViewCell: UITableViewCell {
    
    @IBOutlet weak var Top: NSLayoutConstraint!
    @IBOutlet weak var view: UIView!
    @IBOutlet weak var viewHeight: NSLayoutConstraint!
    
    
    
    @IBOutlet weak var buttom: NSLayoutConstraint!
    @IBOutlet weak var GeneralInfo: UIButton!
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var save: UIButton!
    @IBOutlet weak var back: UIButton!
    
    @IBOutlet weak var Blocktitle: UITextField!
    
    @IBOutlet weak var url: UITextField!
    
    @IBOutlet weak var statusView: Label_DropDown_Combo_View!
    
    
    @IBOutlet weak var stackHeight: NSLayoutConstraint!
    
    @IBOutlet weak var scrollwidth: NSLayoutConstraint!
    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var textView: UITextView!
    
    
    @IBOutlet weak var StoreView: UIView!
    
    @IBOutlet weak var DefautValue: UIButton!
    
    @IBOutlet weak var defaultStore: UIButton!
    @IBOutlet weak var blocktitleLbl: UILabel!
    @IBOutlet weak var identifierLbl: UILabel!
    @IBOutlet weak var storeViewLbl: UILabel!
    @IBOutlet weak var contentLbl: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        blocktitleLbl.text = "Block Title*".localized
        identifierLbl.text = "Identifier*".localized
        storeViewLbl.text = "Store View*".localized
        contentLbl.text = "Content*".localized
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}

