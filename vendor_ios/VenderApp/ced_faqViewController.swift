//
//  ced_faqViewController.swift
//  VenderApp
//
//  Created by Cedcoss on 24/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import FSCalendar
import RxSwift
import RxCocoa

class ced_faqViewController: ced_VendorBaseClass {
    
    //MARK: - stored properties
    
    var faqData = [[String:String]]()
    var currentPage = 1
    var loadMore = true
    var filterParams = ""
    var datePickerView = UIDatePicker()
    var disposeBag = DisposeBag()
    
    //MARK: - UIElements
    
    lazy var topLabel:UIButton = {
        let lbl = UIButton()
        lbl.setTitle("Add FAQ".localized, for: .normal)
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.clipsToBounds = true
        lbl.layer.cornerRadius = 0.0
        lbl.setTitleColor(DynamicColor.ViewBackgroundColor, for: .normal)
        lbl.titleLabel?.font = .systemFont(ofSize: 17, weight: .semibold)
        lbl.addTarget(self, action: #selector(addNewFaqTapped(_:)), for: .touchUpInside)
        return lbl
    }()
    
    lazy var filterBtn:UIButton = {
        let filter = UIButton()
        filter.backgroundColor = DynamicColor.ViewBackgroundColor
        filter.tintColor = DynamicColor.labelColor
        filter.setImage(UIImage(named: "filtericon")?.withRenderingMode(.alwaysTemplate), for: .normal)
        filter.addTarget(self, action: #selector(filterButtonTapped(_:)), for: .touchUpInside)
        return filter
    }()
    
    lazy var faqTable:UITableView = {
        let table = UITableView()
        table.separatorStyle = .none
        table.backgroundColor   = .clear
        table.register(ced_faqItemTableViewCell.self, forCellReuseIdentifier: ced_faqItemTableViewCell.reuseId)
        table.dataSource = self
        table.delegate = self
        return table
    }()
    
    lazy var filterView:ced_faqFilterView = {
        let view = ced_faqFilterView()
        view.publishTo.delegate = self
        view.publishFrom.delegate = self
        view.didSelectFilter = { [weak self] prm in
            print(prm)
            self?.view.viewWithTag(1898)?.removeFromSuperview()
            self?.filterParams = prm
            self?.currentPage = 1
            self?.loadMore = true
            self?.faqData = []
            self?.faqTable.reloadData()
            self?.getFaqData()
        }
        return view
    }()
    
    //MARK: - lifeCycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        currentPage = 1
        loadMore = true
        filterParams = ""
        faqData = []
        faqTable.reloadData()
        getFaqData()
    }
    
    //MARK: - UIHelper
    
    func setupView(){
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        view.addSubview(topLabel)
        view.addSubview(filterBtn)
        view.addSubview(faqTable)
        
        filterBtn.anchor(top: view.safeAreaLayoutGuide.topAnchor, right: view.trailingAnchor, paddingTop: 0, paddingRight: 0, width: 40, height: 40)
        topLabel.anchor(top: view.safeAreaLayoutGuide.topAnchor, left: view.leadingAnchor, right: filterBtn.leadingAnchor, paddingTop: 0, paddingLeft: 5, paddingRight: 5, height: 40)
        faqTable.anchor(top: topLabel.bottomAnchor, left: view.leadingAnchor, bottom: view.safeAreaLayoutGuide.bottomAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 8, paddingBottom: 8, paddingRight: 8)
    }
    
    //MARK: - Selectors
    
    @objc func filterButtonTapped(_ sender:UIButton){
        let bgView = UIView()
        bgView.frame = view.bounds
        view.addSubview(bgView)
        bgView.backgroundColor = UIColor.black.withAlphaComponent(0.5)
        bgView.tag = 1898
        self.addgesturesTohideView(self.view)
        
        bgView.addSubview(filterView)
        filterView.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width - 40, height: 325)
        filterView.center = bgView.center
    }
    
    @objc func addNewFaqTapped(_ sender:UIButton){
        self.navigationController?.pushViewController(ced_addFaqViewController(), animated: true)
    }
    
    //MARK: - API
    
    func getFaqData(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        
        var postString = [String:String]()
        postString["page_size"] = "10"
        postString["current_page"] = "\(currentPage)"
        postString["vendor_id"] = vendorId
        if filterParams != ""{
            postString["filter"] = filterParams
        }
        sendRequest(url: "vfaqapi/faqlist", params: postString)
    }

    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else {return}
        guard let json = try? JSON(data: data) else {return}
        print(json)

        if requestUrl == "vfaqapi/faqlist"{
            if json[0]["success"].stringValue == "true"{
                json[0]["faq_data"].arrayValue.forEach{item in
                    var tempDic = [String:String]()
                    for (key,val) in item.dictionaryValue{
                        tempDic[key] = val.stringValue
                    }
                    faqData.append(tempDic)
                }
            }else{
                loadMore = false
                if currentPage == 1{
                  //  self.view.makeToast(json[0]["msg"].stringValue)
                    self.faqTable.setEmptyMessage(json[0]["msg"].stringValue)
                }
            }
            DispatchQueue.main.async {
                self.faqTable.dataSource = self
                self.faqTable.delegate = self
                self.faqTable.reloadData()
            }
        }
        if requestUrl == "vfaqapi/deletefaq"{
            if json[0]["success"].stringValue == "true"{
                self.view.makeToast(json[0]["msg"].stringValue)
                self.currentPage = 1
                self.filterParams = ""
                self.loadMore = true
                faqData = []
                getFaqData()
            }
        }
    }
    

}
//MARK: - UITableView implementation

extension ced_faqViewController:UITableViewDelegate,UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return faqData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: ced_faqItemTableViewCell.reuseId, for: indexPath) as! ced_faqItemTableViewCell
        cell.parentController = self
        cell.populate(with: faqData[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loadMore{
                self.currentPage += 1;
                self.getFaqData()
            }
        }
    }
    
}
//MARK: - UIGesture
extension ced_faqViewController:UIGestureRecognizerDelegate{
    
    @objc func addgesturesTohideView(_ v:UIView){
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(self.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    @objc func hideView(_ recognizer: UITapGestureRecognizer){
        self.view.viewWithTag(1898)?.removeFromSuperview()
    }
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
        let view = touch.view ?? UIView()
        return !view.isDescendant(of: filterView)
    }
}
//MARK: - texfield delegate
extension ced_faqViewController:UITextFieldDelegate{
    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
        
 
        /* background view */
        let bgCView = UIView();
        bgCView.tag=161;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        
        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorRed = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        let tempInrView = UIView();
        tempInrView.tag=131;
        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: UIScreen.main.bounds.width-100,height: CGFloat(270));
        tempInrView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.backgroundColor = UIColor.white;
        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,y: bgCView.frame.size.height / 2);
        
        let label = UILabel();
        label.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(30));
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        label.text = "Select Date".localized.uppercased();
        label.textAlignment = NSTextAlignment.center;
        label.textColor = UIColor.white;
        label.backgroundColor = color;
        tempInrView.addSubview(label);
        
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.maximumDate = Date()
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
        datePickerView.backgroundColor = UIColor.white;
        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        datePickerView.frame = CGRect(x: 0, y: CGFloat(30), width: tempInrView.frame.width,height: CGFloat(200));
        tempInrView.addSubview(datePickerView);
        
        
        let twoButtonView = TwoButtonView();
        twoButtonView.tag=141;
        twoButtonView.frame = CGRect(x: 0, y: CGFloat(230), width: tempInrView.frame.width,height: CGFloat(40));
        twoButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.addSubview(twoButtonView);
        
        twoButtonView.cancelButton.addTarget(self, action: #selector(cancelDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.cancelButton.backgroundColor = colorRed;
        
        
//        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.backgroundColor = colorGreen;
        twoButtonView.doneButton.rx.tap.bind{
            let dateFormatter = DateFormatter()
            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
            dateFormatter.dateStyle = DateFormatter.Style.short
            dateFormatter.timeStyle = DateFormatter.Style.none
            dateFormatter.dateFormat = "yyyy-MM-dd";
            let timeFormatter = DateFormatter()
            timeFormatter.locale = Locale(identifier: "en_US_POSIX")
            timeFormatter.dateFormat = "hh:mm:ss"
            let time = timeFormatter.string(from: Date())
            let dateDisplay = dateFormatter.string(from: self.datePickerView.date);
            textField.text = dateDisplay + " " + time
            textField.resignFirstResponder();
            self.view.viewWithTag(161)?.removeFromSuperview();
        }.disposed(by: disposeBag)
        
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        bgCView.addSubview(tempInrView);
        self.view.addSubview(bgCView);
  
    }
  
    @objc func cancelDatePickerView(_ sender:UIButton){
        
        sender.resignFirstResponder();
        
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
}
