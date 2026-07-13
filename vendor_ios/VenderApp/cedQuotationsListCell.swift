//
//  cedQuotationsListCell.swift
//  VenderApp
//
//  Created by cedcoss on 16/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedQuotationsListCell: UITableViewCell {

    @IBOutlet weak var idLabel: UILabel!
    
    
    @IBOutlet weak var emailLabel: UILabel!
    
    @IBOutlet weak var productNameLabel: UILabel!
    
    @IBOutlet weak var requestedQtyLabel: UILabel!
    
    @IBOutlet weak var estimatedBudgetPerQty: UILabel!
    
    @IBOutlet weak var statusLabel: UILabel!
    
    @IBOutlet weak var cellView: UIView!
    @IBOutlet weak var actionbtn: UIButton!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
