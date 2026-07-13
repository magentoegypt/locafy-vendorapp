//
//  cedRequestedTransactionCell.swift
//  VenderApp
//
//  Created by cedcoss on 18/02/19.
//  Copyright © 2019 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedRequestedTransactionCell: UITableViewCell {

    @IBOutlet weak var cellViewBottom: UIView!
    @IBOutlet weak var cellViewLeft: UIView!
    @IBOutlet weak var cellView: UIView!
    
    @IBOutlet weak var cellViewBottomImage: UIImageView!
    @IBOutlet weak var cellViewLeftImage: UIImageView!
    @IBOutlet weak var cellViewImage: UIImageView!
    
    @IBOutlet weak var requestedAmountLabel: UILabel!
    
    @IBOutlet weak var requestBtn: UIButton!
    @IBOutlet weak var pendingAmountLabel: UILabel!
    
    @IBOutlet weak var cancelledAmountLabel: UILabel!
    
    
    @IBOutlet weak var requestedButton: UIButton!
    @IBOutlet weak var actionLabel: UILabel!
    @IBOutlet weak var orderIdLabel: UILabel!
    @IBOutlet weak var orderDateLabel: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
