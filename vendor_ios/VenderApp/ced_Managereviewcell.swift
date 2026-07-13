    //
    //  ced_Managereviewcell.swift
    //  VenderApp
    //
    //  Created by cedcoss on 4/3/21.
    //  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
    //

    import UIKit

    class ced_Managereviewcell: UITableViewCell {
        
        
        
        
        @IBOutlet weak var mainstack: UIStackView!
        
        @IBOutlet weak var mainview: UIView!
        
        @IBOutlet weak var lablId: UILabel!
        @IBOutlet weak var id: UILabel!
         
        @IBOutlet weak var Lblcreated: UILabel!
        @IBOutlet weak var lblFrom: UILabel!
        @IBOutlet weak var lbluntil: UILabel!
        
        @IBOutlet weak var lblsituation: UILabel!
        @IBOutlet weak var situation: UILabel!
        @IBOutlet weak var lbltitle: UILabel!
        @IBOutlet weak var title: UILabel!
        
      
        @IBOutlet weak var lblsurname: UILabel!
        @IBOutlet weak var surname: UILabel!
        
        @IBOutlet weak var lblevaluation: UILabel!
        @IBOutlet weak var evaluation: UILabel!
        
        @IBOutlet weak var lblvisibility: UILabel!
        @IBOutlet weak var visibility: UILabel!
        @IBOutlet weak var lbltype: UILabel!
        @IBOutlet weak var type: UILabel!
        @IBOutlet weak var lblproduct: UILabel!
        @IBOutlet weak var product: UILabel!
        
        @IBOutlet weak var lblcode: UILabel!
     
        @IBOutlet weak var code: UILabel!
        @IBOutlet weak var lblaction: UILabel!
        @IBOutlet weak var bttnEdit: UIButton!
        
        var datePickerView = UIDatePicker()
        var globalTextFieldTag = 0;
        var data = JSON()
        var storeDict = [String:String]()
        
        override func awakeFromNib() {
            super.awakeFromNib()
            // Initialization code
            selectionStyle = .none
            mainstack.translatesAutoresizingMaskIntoConstraints=false
            mainstack.topAnchor.constraint(equalTo: mainview.topAnchor,constant: 0).isActive=true
            mainstack.leadingAnchor.constraint(equalTo:mainview.leadingAnchor, constant: 5).isActive=true
            mainstack.trailingAnchor.constraint(equalTo:mainview.trailingAnchor,constant: -5).isActive=true
            mainstack.bottomAnchor.constraint(equalTo: mainview.bottomAnchor,constant: 0).isActive=true
            
            mainview.translatesAutoresizingMaskIntoConstraints=false
            mainview.topAnchor.constraint(equalTo: topAnchor,constant: 0).isActive=false
            mainview.leadingAnchor.constraint(equalTo: leadingAnchor ,constant: 8).isActive=true
            mainview.trailingAnchor.constraint(equalTo: trailingAnchor,constant: -8).isActive=true
            mainview.bottomAnchor.constraint(equalTo: bottomAnchor,constant: 0).isActive=true
            mainview.cardView()
            datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
            populatedata()
//
             
        }

        override func setSelected(_ selected: Bool, animated: Bool) {
            super.setSelected(selected, animated: animated)

            // Configure the view for the selected state
        }
        
        func populatedata()
        {
            
            id.text = data["review_id"].stringValue
            lblFrom.text = data["review_created_at"].stringValue
            situation.text =  data["status"].stringValue
            title.text = data["title"].stringValue
            surname.text = data["nickname"].stringValue
            evaluation.text =  data["detail"].stringValue
            type.text = data["type"].stringValue
            product.text = data["name"].stringValue
            code.text = data["sku"].stringValue
            
            var visibilityArr = [String]()
            data["store_id"].arrayValue.forEach{visibilityArr.append(storeDict[$0.stringValue] ?? "")}
            visibility.text = visibilityArr.joined(separator: ",")
        
        }
          

    }
