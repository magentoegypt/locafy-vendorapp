//
//  ced_memberShipImageTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 21/09/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_memberShipImageTableViewCell: UITableViewCell {

    //MARK:- Stored properties
    
    static var reuseID = "ced_memberShipImageTableViewCell"
  
    //MARK:- Init
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        initViews()
    }
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    //MARK:- UIElements
    
    lazy var planImage:UIImageView = {
        let image = UIImageView()
        image.contentMode = .scaleAspectFit
        return image
    }()
    
    func initViews(){
        addSubview(planImage)
        planImage.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 15, paddingRight: 15, height: 245)
    }
    
    var imagePlan : String?{
        didSet{
            guard let image = imagePlan else {return}
            planImage.sd_setImage(with: URL(string: image), placeholderImage: UIImage(named: "placeholder"))
        }
    }

}
