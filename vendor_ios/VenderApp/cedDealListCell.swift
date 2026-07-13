//
//  cedDealListCell.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedDealListCell: UITableViewCell {

    @IBOutlet weak var dealId: UILabel!
    
    @IBOutlet weak var productName: UILabel!
    
    @IBOutlet weak var dealPrice: UILabel!
    
    @IBOutlet weak var dealEnd: UILabel!
    
    @IBOutlet weak var dealStatus: UILabel!
    
    @IBOutlet weak var adminStatus: UILabel!
    
    @IBOutlet weak var editButton: UIButton!
    
    /*----deal products---*/
    @IBOutlet weak var cellView: UIView!
    
    @IBOutlet weak var productId: UILabel!
    
    @IBOutlet weak var name: UILabel!
    
    @IBOutlet weak var type: UILabel!
    
    @IBOutlet weak var price: UILabel!
    
    @IBOutlet weak var qty: UILabel!
    
    @IBOutlet weak var status: UILabel!
    
    @IBOutlet weak var createDeal: UIButton!
    
    @IBOutlet weak var prodIdLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var typeLbl: UILabel!
    @IBOutlet weak var priceLbl: UILabel!
    @IBOutlet weak var qtyLbl: UILabel!
    @IBOutlet weak var statusLbl: UILabel!
    
    @IBOutlet weak var dealIdLbl: UILabel!
    @IBOutlet weak var prodNameLbl: UILabel!
    @IBOutlet weak var dealPriceLbl: UILabel!
    @IBOutlet weak var dealEndLbl: UILabel!
    @IBOutlet weak var dealStatusLbl: UILabel!
    @IBOutlet weak var adminStatusLbl: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
       
        
       
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
