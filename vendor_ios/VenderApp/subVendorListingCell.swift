//
//  subVendorListingCell.swift
//  VenderApp
//
//  Created by MacMini on 24/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class subVendorListingCell: UITableViewCell {

    @IBOutlet weak var firstName: UILabel!
    
    @IBOutlet weak var lastName: UILabel!
    
    @IBOutlet weak var email: UILabel!
    
    
    @IBOutlet weak var status: UILabel!
    
    @IBOutlet weak var approveButton: UIButton!
    
    @IBOutlet weak var disapproveButton: UIButton!
    
    
    @IBOutlet weak var deleteButton: UIButton!
    
    
    
    
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
