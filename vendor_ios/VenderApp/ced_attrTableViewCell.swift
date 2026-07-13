//
//  ced_attrTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 13/02/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_attrTableViewCell: UITableViewCell {

    @IBOutlet weak var delete: UIButton!
    
    @IBOutlet weak var attrValue: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
