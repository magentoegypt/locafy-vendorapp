//
//  MultiSelectCollectionViewCell.swift
//  VenderApp
//
//  Created by Macmini on 19/12/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class MultiSelectCollectionViewCell: UICollectionViewCell {

    
    @IBOutlet weak var checkBoxButtonView: CheckboxButtonView!
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }
    @objc func setdataView(dataFor checkboxSelected : [String]){
        if checkboxSelected.contains(checkBoxButtonView.checkboxButton.currentTitle ?? ""){
            checkBoxButtonView.checkboxButton.setImage(UIImage(named: "checked"), for: .normal)
        }else{
            checkBoxButtonView.checkboxButton.setImage(UIImage(named: "unchecked"), for: .normal)
        }
    }

}
