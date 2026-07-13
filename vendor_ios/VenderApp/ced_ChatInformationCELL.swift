//
//  ced_ChatInformationCELL.swift
//  VenderApp
//
//  Created by Macmini on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_ChatInformationCELL: UITableViewCell {

    @IBOutlet weak var nfileselected: UILabel!
    
    @IBOutlet weak var toplable: UILabel!
    
    @IBOutlet weak var sendmessage: UIButton!
    @IBOutlet weak var browseButton: UIButton!
    @IBOutlet weak var MessageTextFeild: UITextView!
    
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var attachlbl: UILabel!
    
    @IBOutlet weak var cellview: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        cellview.makeCard(cellview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
