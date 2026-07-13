//
//  ced_ChatHistoryTC.swift
//  VenderApp
//
//  Created by cedcoss on 07/07/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_ChatHistoryTC: UITableViewCell {
static var reusedID = "ced_ChatHistoryTC"
    
//    lazy var topHeading:UILabel = {
//        let label = UILabel()
//        label.text = "Chat History"
//        label.font = .systemFont(ofSize: 16, weight: .semibold)
//        label.textAlignment = .center
//        return label
//    }()
    lazy var senderName:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14, weight: .regular)
        return label
    }()
    lazy var createdAt : UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14, weight: .regular)
        return label
    }()
    lazy var numberOfLicence: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14, weight: .regular)
        return label
    }()
    lazy var estimatedBudgetPerLicence: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14, weight: .regular)
        return label
    }()
    lazy var comments: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14, weight: .regular)
        return label
    }()
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func configureView(){
        backgroundColor = DynamicColor.ViewBackgroundColor
//        addSubview(topHeading)
//        topHeading.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 5,  paddingRight: 5,  height: 40)
        addSubview(senderName)
        senderName.anchor(top: topAnchor, left: leadingAnchor, paddingTop: 8, paddingLeft: 8,width: 75, height: 30)
        addSubview(createdAt)
        createdAt.anchor(top: topAnchor, left: senderName.trailingAnchor, right: trailingAnchor, paddingTop: 8, paddingLeft: 8, paddingRight: 8, height: 30)
        addSubview(numberOfLicence)
        numberOfLicence.anchor(top: senderName.bottomAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 2, paddingLeft: 8, paddingRight: 8, height: 30)
        addSubview(estimatedBudgetPerLicence)
        estimatedBudgetPerLicence.anchor(top: numberOfLicence.bottomAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 2, paddingLeft: 8,  paddingRight: 8,  height: 30)
        addSubview(comments)
        comments.anchor(top: estimatedBudgetPerLicence.bottomAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 2, paddingLeft: 8, paddingRight: 8, height: 30)
    }
    func populateData(data:ChatHistoryModel){
        self.comments.text = "Comment:   "+data.comment
        self.createdAt.text = data.created_at
        self.senderName.text = data.sender_name + " | "
        self.senderName.textColor = DynamicColor.themeColor
        self.senderName.sizeToFit()
        if data.sender_name.lowercased() == "Me".lowercased(){
            self.senderName.textColor = DynamicColor.secondaryColor
        }
        self.numberOfLicence.text = "Number of Licences:   "+data.negotiation_qty
        self.estimatedBudgetPerLicence.text = "Estimated Budget Per Licence:   "+data.negotiation_price
    }
}
