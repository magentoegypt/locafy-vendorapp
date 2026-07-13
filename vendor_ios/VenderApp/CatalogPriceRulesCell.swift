//
//  CatalogPriceRulesCell.swift
//  VenderApp
//
//  Created by Macmini on 11/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class CatalogPriceRulesCell: UITableViewCell {
    
    
    @IBOutlet weak var priorityheight: NSLayoutConstraint!
    
    @IBOutlet weak var priorityheightt: NSLayoutConstraint!
    
    @IBOutlet weak var coupancodeheight: NSLayoutConstraint!
    
    @IBOutlet weak var coupancodeheightt: NSLayoutConstraint!
    
    @IBOutlet weak var status: UILabel!
    @IBOutlet weak var id: UILabel!
    
    @IBOutlet weak var rule: UILabel!
    
    
    @IBOutlet weak var coupancodetextfeild: UILabel!
    
    @IBOutlet weak var website: UILabel!
    @IBOutlet weak var end: UILabel!
    @IBOutlet weak var start: UILabel!
    
    
    @IBOutlet weak var coupancode: UILabel!
    
    @IBOutlet weak var priority: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
