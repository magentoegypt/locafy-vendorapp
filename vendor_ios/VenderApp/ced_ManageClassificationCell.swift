//
//  ced_ManageClassificationCell.swift
//  VenderApp
//
//  Created by cedcoss on 4/3/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_ManageClassificationCell: UITableViewCell {
    
    var dat = JSON()
    var index = 0
    @IBOutlet weak var mainstack: UIStackView!
    
    @IBOutlet weak var mainview: UIView!
    
    
    @IBOutlet weak var lblid: UILabel!
    @IBOutlet weak var id: UILabel!
    @IBOutlet weak var lblclassification: UILabel!
    @IBOutlet weak var classificatn: UILabel!
    @IBOutlet weak var lblordering: UILabel!
    @IBOutlet weak var ordring: UILabel!
    @IBOutlet weak var lblisactive: UILabel!
    @IBOutlet weak var bttnactive: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
       
        // Initialization code
        selectionStyle = .none
 
     }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
        mainview.cardView()
    }

    func populatedata()
    {
        
        
        print(index)
        id.text = dat[0]["vendor_data"]["rating_list"][index]["rating_id"].stringValue
        print(dat[0]["vendor_data"]["rating_list"].stringValue)
        classificatn.text = dat[0]["vendor_data"]["rating_list"][index]["rating_code"].stringValue
        ordring.text = dat[0]["vendor_data"]["rating_list"][index]["sort_order"].stringValue
        
        if dat[0]["vendor_data"]["rating_list"][index]["is_active"].intValue == 1
        { bttnactive.text = "Active"}
        else
        { bttnactive.text = "Inactive"}
       
    }
    

}
