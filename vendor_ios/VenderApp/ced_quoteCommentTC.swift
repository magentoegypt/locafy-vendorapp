//
//  ced_quoteCommentTC.swift
//  VenderApp
//
//  Created by cedcoss on 08/07/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_quoteCommentTC: UITableViewCell {
    static var reuseId = "ced_quoteCommentTC"
    lazy var headingLabel : UILabel = {
        let label = UILabel()
        label.text = ""
        label.font = .systemFont(ofSize: 16, weight: .semibold)
        label.textColor = DynamicColor.labelColor
        label.textAlignment = .center
        return label
    }()
    lazy var commentView: UITextView = {
        let textView = UITextView()
        textView.font = .systemFont(ofSize: 14, weight: .regular)
        textView.layer.cornerRadius = 2.0
        textView.layer.borderWidth = 1.0
        textView.layer.borderColor = UIColor.init(hexString: "#f2f2f2")?.cgColor
        return textView
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
        addSubview(headingLabel)
        headingLabel.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 8, paddingRight: 8,height: 40)
        addSubview(commentView)
        commentView.anchor(top: headingLabel.bottomAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingBottom: 5, paddingRight: 5)
    }
}
