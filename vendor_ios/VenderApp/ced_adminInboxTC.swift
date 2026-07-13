//
//  ced_adminInboxTC.swift
//  VenderApp
//
//  Created by cedcoss on 12/02/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_adminInboxTC: UITableViewCell {


    @IBOutlet weak var adminSenderLabel: UILabel!
    @IBOutlet weak var adminRecieverLabel: UILabel!
    @IBOutlet weak var adminCreatedByLabel: UILabel!
    @IBOutlet weak var adminUpdatedAtLabel: UILabel!
    @IBOutlet weak var adminSubjectLabel: UILabel!
    @IBOutlet weak var newMessageLabel: UILabel!
    
    @IBOutlet var cellCardView: UIView!

    @IBOutlet weak var adminViewBtn: UIButton!
  
    @IBOutlet weak var senderLbl: UILabel!
    @IBOutlet weak var recieverLbl: UILabel!
    @IBOutlet weak var createdAtLbl: UILabel!
    @IBOutlet weak var updatedAtLbl: UILabel!
    @IBOutlet weak var subjectLbl: UILabel!
    @IBOutlet weak var newMsgLbl: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        adminViewBtn.layer.cornerRadius = 5
        senderLbl.text = "Sender".localized
        recieverLbl.text = "Receiver".localized
        createdAtLbl.text = "Created At".localized
        updatedAtLbl.text = "Update At".localized
        subjectLbl.text = "Subject".localized
        newMsgLbl.text = "New message".localized
        adminViewBtn.setTitle("View".localized, for: .normal)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
    }
    
    
}
