//
//  ced_PO_Detail_Cell.swift
//  VenderApp
//
//  Created by Macmini on 24/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_PO_Detail_Cell: UITableViewCell {
    @IBOutlet weak var product: UILabel!
    @IBOutlet weak var sku: UILabel!
    
    @IBOutlet weak var remainingqty: UILabel!
    
    @IBOutlet weak var rowtotal: UILabel!
    @IBOutlet weak var toplable: UILabel!
    @IBOutlet weak var cellview: UIView!
    
    @IBOutlet weak var requestedQty: UILabel!
    
    @IBOutlet weak var requestedPrice: UILabel!
    @IBOutlet weak var proposedQty: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
