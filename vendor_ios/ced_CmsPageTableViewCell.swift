//
//  ced_CmsPageTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 08/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_CmsPageTableViewCell: UITableViewCell {
    
    var iscontentLoaded = true
    var isDesignLoaded = true
    @IBOutlet weak var save: UIButton!
    @IBOutlet weak var content: UIButton!
    @IBOutlet weak var scrollWidth: NSLayoutConstraint!
    @IBOutlet weak var stackHeight: NSLayoutConstraint!
    @IBOutlet weak var storeStack: UIStackView!
    @IBOutlet weak var layoutDropdown: Label_DropDown_Combo_View!
    @IBOutlet weak var top: NSLayoutConstraint!
    @IBOutlet weak var ContentHeight: NSLayoutConstraint!
    @IBOutlet weak var contentHeading: UITextField!
    @IBOutlet weak var ContentDescribtion: UITextView!
    @IBOutlet weak var MainTitleHeading: UILabel!
    @IBOutlet weak var MainTitle: UILabel!
    @IBOutlet weak var Viewheight: NSLayoutConstraint!
    @IBOutlet weak var TopSpace: NSLayoutConstraint!
    @IBOutlet weak var PageInfo: UIButton!
    @IBOutlet weak var pageTitle: UITextField!
    @IBOutlet weak var urlKey: UITextField!
    @IBOutlet weak var StoreView: UIButton!
    @IBOutlet weak var VendorPage: Label_DropDown_Combo_View!
    @IBOutlet weak var status: Label_DropDown_Combo_View!
    @IBOutlet weak var Desgin: UIButton!
    @IBOutlet weak var LayoutTitle: UITextField!
    @IBOutlet weak var LayoutDescribtion: UITextView!
    @IBOutlet weak var DesignHeight: NSLayoutConstraint!
    @IBOutlet weak var DesignTop: NSLayoutConstraint!
    @IBOutlet weak var MetaButton: UIButton!
    @IBOutlet weak var keyword: UITextView!
    @IBOutlet weak var metaViewTop: NSLayoutConstraint!
    @IBOutlet weak var MetaViewHeight: NSLayoutConstraint!
    @IBOutlet weak var metaDescribtion: UITextView!
    @IBOutlet weak var DeFaultValue: UIButton!
    @IBOutlet weak var DefaultStore: UIButton!
    @IBOutlet weak var PageView: UIView!
    @IBOutlet weak var contentView2: UIView!
    @IBOutlet weak var MetaView: UIView!
    @IBOutlet weak var DesignView: UIView!
    
    @IBOutlet weak var pageTitleLbl: UILabel!
    @IBOutlet weak var urlKeyLbl: UILabel!
    @IBOutlet weak var storeViewLbl: UILabel!
    
    @IBOutlet weak var contentHeadingLbl: UILabel!
    @IBOutlet weak var contentLbl: UILabel!
    
    @IBOutlet weak var layoutXmlLbl: UILabel!
    @IBOutlet weak var keyboardLbl: UILabel!
    @IBOutlet weak var descriptionLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}

