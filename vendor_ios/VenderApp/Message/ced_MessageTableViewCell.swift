//
//  ced_MessageTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_MessageTableViewCell: UITableViewCell {

    @IBOutlet weak var sendername: UILabel!
    @IBOutlet weak var NoMessage: UILabel!
    @IBOutlet weak var sentCell: UIView!
    @IBOutlet weak var inboxCell: UIView!
    @IBOutlet weak var message: UILabel!
    @IBOutlet weak var sendermail: UILabel!
    
    @IBOutlet weak var subject: UILabel!
    
    @IBOutlet weak var Date: UILabel!
    
    @IBOutlet weak var InboxTitle: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
