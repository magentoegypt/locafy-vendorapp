//
//  ced_customerInboxTC.swift
//  VenderApp
//
//  Created by cedcoss on 13/02/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_customerInboxTC: UITableViewCell {
    @IBOutlet weak var senderLabel: UILabel!
    @IBOutlet weak var receiverLabel: UILabel!
    @IBOutlet weak var createdAtLabel: UILabel!
    @IBOutlet weak var updatedAtLabel: UILabel!
    @IBOutlet weak var subjectLabel: UILabel!
    @IBOutlet weak var newMessageLabel: UILabel!
    @IBOutlet weak var viewButton: UIButton!
    
    @IBOutlet weak var outerView: UIView!
    @IBOutlet weak var senderLbl: UILabel!
    @IBOutlet weak var recieverLbl: UILabel!
    @IBOutlet weak var createdLbl: UILabel!
    @IBOutlet weak var updatedLbl: UILabel!
    @IBOutlet weak var subjectLbl: UILabel!
    @IBOutlet weak var newMsgLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        outerView.cardView()
        viewButton.layer.cornerRadius = 5
        senderLbl.text = "Sender".localized
        recieverLbl.text = "Receiver".localized
        createdLbl.text = "Created At".localized
        updatedLbl.text = "Updated At".localized
        subjectLbl.text = "Subject".localized
        newMsgLbl.text = "New Message".localized
        viewButton.setTitle("VIEW".localized, for: .normal)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
