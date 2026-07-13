//
//  cedDealCell.swift
//  VenderApp
//
//  Created by cedcoss on 04/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedDealCell: UITableViewCell {

    @IBOutlet weak var headingLabel: UILabel!
    @IBOutlet weak var headingView: UIView!
    @IBOutlet weak var createDealButton: UIButton!
    
    @IBOutlet weak var backButton: UIButton!
    
    @IBOutlet weak var deleteButton: UIButton!
    @IBOutlet weak var fieldsView: UIView!
    
    @IBOutlet weak var productId: UITextField!
    
    @IBOutlet weak var productName: UITextField!
    
    @IBOutlet weak var dealStatus: UIButton!
    
    @IBOutlet weak var dealPrice: UITextField!
    
    @IBOutlet weak var dealTo: UIButton!
    @IBOutlet weak var dealFrom: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
