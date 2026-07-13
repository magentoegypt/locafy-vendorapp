//
//  bidCell.swift
//  VenderApp
//
//  Created by MacMini on 23/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class bidCell: UITableViewCell {

    @IBOutlet weak var productId: UILabel!
    
    @IBOutlet weak var customerId: UILabel!
    
    @IBOutlet weak var customerName: UILabel!
    
    @IBOutlet weak var bidPrice: UILabel!
    
    @IBOutlet weak var bidDate: UILabel!
    
    @IBOutlet weak var bidTime: UILabel!
    
    @IBOutlet weak var winner: UILabel!
    
    @IBOutlet weak var status: UILabel!
    
    @IBOutlet weak var approvedButton: UIButton!
    
    @IBOutlet weak var disapprovedButton: UIButton!
    
    @IBOutlet weak var viewForCard: UIView!
    
    
    
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
