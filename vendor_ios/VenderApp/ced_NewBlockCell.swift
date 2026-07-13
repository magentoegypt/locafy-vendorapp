//
//  ced_NewBlockCell.swift
//  VenderApp
//
//  Created by cedcoss on 6/24/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_NewBlockCell: UITableViewCell {
    
    
    @IBOutlet weak var id: UILabel!
    @IBOutlet weak var status: UILabel!
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var approved: UILabel!
    @IBOutlet weak var layout: UILabel!
    @IBOutlet weak var created: UILabel!
    @IBOutlet weak var lastmodified: UILabel!
    @IBOutlet weak var lblurl: UILabel!
 
    @IBOutlet weak var mainview: UIView!
    
    @IBOutlet weak var layoutstack: UIStackView!
    @IBOutlet weak var bttnview: TwoButtonView!
    
    @IBOutlet weak var idLbl: UILabel!
    @IBOutlet weak var statusLbl: UILabel!
    @IBOutlet weak var titleLbl: UILabel!
    @IBOutlet weak var approvedLbl: UILabel!
    @IBOutlet weak var layoutLbl: UILabel!
    @IBOutlet weak var createdLbl: UILabel!
    @IBOutlet weak var modifiedLbl: UILabel!
    @IBOutlet weak var urlKeyLbl: UILabel!
    
    
    var data : JSON!
    var isLoadedFrom:Bool!
    override func awakeFromNib() {
        super.awakeFromNib()
        
        mainview.cardView()
        
        idLbl.text = "Id".localized
        statusLbl.text = "Status".localized
        titleLbl.text = "Title".localized
        approvedLbl.text = "Approved".localized
        layoutLbl.text = "Layout".localized
        createdLbl.text = "Created".localized
        modifiedLbl.text = "Last Modified".localized
        urlKeyLbl.text = "URL Key".localized
        // Initialization code
    }
    
    func PopulateData()
    {
        let  colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        print(data["page_id"].stringValue)
        if isLoadedFrom
        {
            id.text = data["page_id"].stringValue
            layout.text = data["page_layout"].stringValue
        }
        else
        {
            id.text = data["block_id"].stringValue
        }
        
        
     //   id.text = data["page_id"].stringValue
        
        let active = data["is_active"].boolValue
        if(active)
        {
             status.text = "Enable"
             status.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
           // statusData = cell.status.text!
        }
        else
        {
             status.text = "Disabled"
             status.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
          //  statusData = cell.status.text!
        }
        
        
        title.text = data["title"].stringValue
        
        let approve = data["is_approve"].boolValue
     
        if approve 
        {
             approved.text = "Approved"
             approved.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        }
        else
        {
            approved.text = "Pending"
            approved.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        }
        
        if isLoadedFrom 
        {
            layoutstack.isHidden=false
            
//             LayoutHeigth.constant = 0
//             LayoutContent.constant = 0
        }
        else
        {
            layoutstack.isHidden=true
        }
        
        layout.text = data["page_layout"].stringValue
        created.text = data["creation_time"].stringValue
        lastmodified.text = data["update_time"].stringValue
        lblurl.text = data["identifier"].stringValue
        
 
       
       
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}

 
