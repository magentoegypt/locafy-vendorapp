//
//  ced_ChatHistoryInformationCELL.swift
//  VenderApp
//
//  Created by Macmini on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_ChatHistoryInformationCELL: UITableViewCell {

    @IBOutlet weak var toplable: UILabel!
    @IBOutlet weak var toplableheight: NSLayoutConstraint!
    
    @IBOutlet weak var chat_file: UIButton!
    @IBOutlet weak var chathistoryheight: NSLayoutConstraint!
    
    @IBOutlet weak var typevenderorbuyer: UILabel!
    
    @IBOutlet weak var message: UILabel!
    @IBOutlet weak var dat: UILabel!
    
    
    @IBOutlet weak var chathistoryview: UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        chathistoryview.makeCard(chathistoryview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
