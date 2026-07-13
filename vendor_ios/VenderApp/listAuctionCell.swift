//
//  listAuctionCell.swift
//  VenderApp
//
//  Created by MacMini on 22/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class listAuctionCell: UITableViewCell {

    @IBOutlet weak var viewForCard: UIView!
    
    @IBOutlet weak var viewButton: UIButton!
    
    @IBOutlet weak var editButton: UIButton!
    
    @IBOutlet weak var deleteButton: UIButton!
    
    @IBOutlet weak var productId: UILabel!
    
    @IBOutlet weak var productName: UILabel!
    
    @IBOutlet weak var startPrice: UILabel!
    
    @IBOutlet weak var maxPrice: UILabel!
    
    @IBOutlet weak var startDate: UILabel!
    
    @IBOutlet weak var endDate: UILabel!
    
    @IBOutlet weak var sellProduct: UILabel!
    
    @IBOutlet weak var status: UILabel!
    
    @IBOutlet weak var vpaStatus: UILabel!
    
    
    
    
    
    
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
