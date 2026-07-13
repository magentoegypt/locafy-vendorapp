//
//  CustomCheckboxControl.swift
//  LocafyApp
//
//  Created by Apple on 23/03/2025.
//  Copyright © 2025 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

@IBDesignable
class CustomCheckboxControl: UIControl {

    @IBInspectable var isChecked: Bool = false {
        didSet {
            updateAppearance()
        }
    }

    @IBInspectable var title: String = "Checkbox" {
        didSet {
            label.text = title
        }
    }

    @IBInspectable var checkColor: UIColor = .systemBlue {
        didSet {
            updateAppearance()
        }
    }

    private let imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        return imageView
    }()

    private let label: UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize: 16)
        label.textColor = .black
        return label
    }()

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }

    private func setupView() {
        addSubview(imageView)
        addSubview(label)

        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(toggleCheck))
        addGestureRecognizer(tapGesture)

        updateAppearance()
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        
        let imageSize: CGFloat = 24
        imageView.frame = CGRect(x: 0, y: (bounds.height - imageSize) / 2, width: imageSize, height: imageSize)
        label.frame = CGRect(x: imageSize + 8, y: 0, width: bounds.width - imageSize - 8, height: bounds.height)
    }

    @objc private func toggleCheck() {
        isChecked.toggle()
        sendActions(for: .valueChanged)  // Notify listeners
    }

    private func updateAppearance() {
        let imageName = isChecked ? "checkmark.square.fill" : "square"
        imageView.image = UIImage(systemName: imageName)
        imageView.tintColor = isChecked ? checkColor : .gray
    }
}

@IBDesignable
class CustomCheckbox: UIView {

    // MARK: - Properties
    private let checkboxButton: UIButton = UIButton(type: .system)
    private let titleLabel: UILabel = UILabel()
    private let editButton: UIButton = UIButton(type: .system)
    private let deleteButton: UIButton = UIButton(type: .system)

    @IBInspectable var title: String = "Checkbox Title" {
        didSet { titleLabel.text = title }
    }

    @IBInspectable var isChecked: Bool = false {
        didSet { updateCheckboxUI() }
    }
    
    var index:Int = 0
    weak var delegate: CustomCheckboxDelegate?
    

    // MARK: - Init
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }

    // MARK: - Setup View
    private func setupView() {
        self.layer.cornerRadius = 8
       // self.backgroundColor = UIColor.systemGray6

        // Configure Checkbox Button
        checkboxButton.addTarget(self, action: #selector(toggleCheckbox), for: .touchUpInside)
        checkboxButton.tintColor = UIColor(hexString: "#3F89E6")!
        updateCheckboxUI()

        // Configure Title Label
        titleLabel.text = title
        titleLabel.font = UIFont.systemFont(ofSize: 16)
        titleLabel.textColor = .black

        // Configure Edit Button
        editButton.setImage(UIImage(systemName: "pencil"), for: .normal)
        editButton.tintColor = UIColor(hexString: "#002F63")!
        editButton.addTarget(self, action: #selector(editTapped), for: .touchUpInside)

        // Configure Delete Button
        deleteButton.setImage(UIImage(systemName: "trash"), for: .normal)
        deleteButton.tintColor = UIColor(hexString: "#002F63")!
        deleteButton.addTarget(self, action: #selector(deleteTapped), for: .touchUpInside)

        // Layout Components
        let stackView = UIStackView(arrangedSubviews: [checkboxButton, titleLabel, editButton, deleteButton])
        stackView.axis = .horizontal
        stackView.spacing = 10
        stackView.alignment = .leading
        stackView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(stackView)

        NSLayoutConstraint.activate([
            stackView.leadingAnchor.constraint(equalTo: self.leadingAnchor, constant: 0),
            stackView.trailingAnchor.constraint(equalTo: self.trailingAnchor, constant: 0),
            stackView.topAnchor.constraint(equalTo: self.topAnchor, constant: 10),
            stackView.bottomAnchor.constraint(equalTo: self.bottomAnchor, constant: -10),

            checkboxButton.widthAnchor.constraint(equalToConstant: 20),
            checkboxButton.heightAnchor.constraint(equalToConstant: 20),
            editButton.widthAnchor.constraint(equalToConstant: 20),
            editButton.heightAnchor.constraint(equalToConstant: 20),
            deleteButton.widthAnchor.constraint(equalToConstant: 20),
            deleteButton.heightAnchor.constraint(equalToConstant: 20),
        ])
    }

    // MARK: - UI Updates
    private func updateCheckboxUI() {
        let imageName = isChecked ? "checkmark.square.fill" : "square"
        checkboxButton.setImage(UIImage(systemName: imageName), for: .normal)
    }
    
    func hideEditDeleteButtons(){
        editButton.isHidden = true
        deleteButton.isHidden = true
    }

    // MARK: - Actions
    @objc private func toggleCheckbox() {
        isChecked.toggle()
        updateCheckboxUI()
        delegate?.checkboxChanged(self)
    }

    @objc private func editTapped() {
        delegate?.didTapEdit(self)
    }

    @objc private func deleteTapped() {
        delegate?.didTapDelete(self)
    }
}

// MARK: - Delegate Protocol
protocol CustomCheckboxDelegate: AnyObject {
    func didTapEdit(_ checkbox: CustomCheckbox)
    func didTapDelete(_ checkbox: CustomCheckbox)
    func checkboxChanged(_ checkbox: CustomCheckbox)
}


@IBDesignable
class AttributeTitleView: UIView {

    // MARK: - Properties
    private let titleLabel: UILabel = UILabel()
    let selectButton: UIButton = UIButton(type: .system)
    let deSelectButton: UIButton = UIButton(type: .system)
    let deleteButton: UIButton = UIButton(type: .system)

    @IBInspectable var title: String = "Checkbox Title" {
        didSet { titleLabel.text = title }
    }


    // MARK: - Init
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }

    // MARK: - Setup View
    private func setupView() {
        self.layer.cornerRadius = 8
       // self.backgroundColor = UIColor.systemGray6

        // Configure Checkbox Button
        
        

        // Configure Title Label
        titleLabel.text = title
        titleLabel.font = UIFont.systemFont(ofSize: 16)
        selectButton.titleLabel?.font =  UIFont.systemFont(ofSize: 12)
        deSelectButton.titleLabel?.font =  UIFont.systemFont(ofSize: 12)
        titleLabel.textColor = .black
        selectButton.setTitleColor(UIColor(hexString: "#002F63"), for: .normal)
        deSelectButton.setTitleColor(UIColor(hexString: "#002F63"), for: .normal)
        // Configure Edit Button
       
        selectButton.setTitle("Select All".localized, for: .normal)
        deSelectButton.setTitle("DeSelect All".localized, for: .normal)
        // Configure Delete Button
        deleteButton.setImage(UIImage(systemName: "trash"), for: .normal)
        deleteButton.tintColor = UIColor(hexString: "#002F63")!


        // Layout Components
        let stackView = UIStackView(arrangedSubviews: [titleLabel, selectButton,deSelectButton, deleteButton])
        stackView.axis = .horizontal
        stackView.spacing = 10
        stackView.alignment = .leading
        stackView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(stackView)

        NSLayoutConstraint.activate([
            stackView.leadingAnchor.constraint(equalTo: self.leadingAnchor, constant: 0),
            stackView.trailingAnchor.constraint(equalTo: self.trailingAnchor, constant: 0),
            stackView.topAnchor.constraint(equalTo: self.topAnchor, constant: 10),
            stackView.bottomAnchor.constraint(equalTo: self.bottomAnchor, constant: -10),
            deleteButton.widthAnchor.constraint(equalToConstant: 20),
            deleteButton.heightAnchor.constraint(equalToConstant: 20),
        ])
    }

    // MARK: - UI Updates
    
}


@IBDesignable class PaddingLabel: UILabel {

    @IBInspectable var topInset: CGFloat = 8.0
    @IBInspectable var bottomInset: CGFloat = 8.0
    @IBInspectable var leftInset: CGFloat = 8.0
    @IBInspectable var rightInset: CGFloat = 8.0

    override func drawText(in rect: CGRect) {
        let insets = UIEdgeInsets(top: topInset, left: leftInset, bottom: bottomInset, right: rightInset)
        super.drawText(in: rect.inset(by: insets))
    }

    override var intrinsicContentSize: CGSize {
        let size = super.intrinsicContentSize
        return CGSize(width: size.width + leftInset + rightInset,
                      height: size.height + topInset + bottomInset)
    }

    override var bounds: CGRect {
        didSet {
            // ensures this works within stack views if multi-line
            preferredMaxLayoutWidth = bounds.width - (leftInset + rightInset)
        }
    }
}
