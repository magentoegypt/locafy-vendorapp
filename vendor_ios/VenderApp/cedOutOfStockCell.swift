//
//  cedOutOfStockCell.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedOutOfStockCell: UITableViewCell {
    @IBOutlet weak var sku: UILabel!
    
    @IBOutlet weak var productName: UILabel!
    
    @IBOutlet weak var productType: UILabel!
    
    @IBOutlet weak var quantity: UILabel!
    
    @IBOutlet weak var viewsLabel: UILabel!
    
    @IBOutlet weak var cellView: UIView!
    
    @IBOutlet weak var skuLbl: UILabel!
    @IBOutlet weak var productNameLbl: UILabel!
    @IBOutlet weak var prodTypeLbl: UILabel!
    @IBOutlet weak var qtyLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
