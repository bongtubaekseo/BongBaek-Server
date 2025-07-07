package org.appjam.bongbaek.domain.event.entity;

import java.time.LocalDate;
import java.util.UUID;

import org.appjam.bongbaek.domain.common.BaseEntity;
import org.appjam.bongbaek.domain.event.dto.request.EventUpdateRequestDto;
import org.appjam.bongbaek.domain.member.entity.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
		name = "event",
		indexes = {
				@Index(name = "idx_member_id", columnList = "member_id")
		}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {
	@Id
	@Column(name = "event_id", columnDefinition = "BINARY(16)")
	private UUID eventId;

	// host
	@Column(name = "host_name", length = 30, nullable = false)
	private String hostName;

	@Column(name = "host_nickname", length = 30, nullable = false)
	private String hostNickname;

	@Enumerated(EnumType.STRING)
	@Column(name = "relationship", columnDefinition = "VARCHAR(50)", nullable = false)
	private Relationship relationship;

	@Column(name = "contact_frequency")
	private int contactFrequency;

	@Column(name = "meet_frequency")
	private int meetFrequency;

	// event
	@Enumerated(EnumType.STRING)
	@Column(name = "event_category", columnDefinition = "VARCHAR(50)", nullable = false)
	private Category eventCategory;

	@Column(name = "event_date", nullable = false)
	private LocalDate eventDate;

	@Column(name = "is_attend", nullable = false)
	private boolean attended;

	@Column(name = "note", columnDefinition = "TEXT")
	private String note;

	@Column(name = "cost", nullable = false)
	private int cost;

	// location
	@Column(name = "location", nullable = false)
	private String location;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "latitude", nullable = false)
	private double latitude;

	@Column(name = "longitude", nullable = false)
	private double longitude;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder
	private Event(String hostName, String hostNickname, Relationship relationship, int contactFrequency,
			int meetFrequency,
			Category eventCategory, LocalDate eventDate, boolean attended, String note, int cost,
			String location, String address, double latitude, double longitude, Member member) {
		this.eventId = UUID.randomUUID();
		this.hostName = hostName;
		this.hostNickname = hostNickname;
		this.relationship = relationship;
		this.contactFrequency = contactFrequency;
		this.meetFrequency = meetFrequency;
		this.eventCategory = eventCategory;
		this.eventDate = eventDate;
		this.attended = attended;
		this.note = note;
		this.cost = cost;
		this.location = location;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.member = member;
	}

	public void updateFromDto(EventUpdateRequestDto dto) {
		this.hostName = dto.hostInfo().hostName();
		this.hostNickname = dto.hostInfo().hostNickname();
		this.eventCategory = dto.eventInfo().eventCategory();
		this.relationship = dto.eventInfo().eventRelationship();
		this.cost = dto.eventInfo().getCost();
		this.attended = dto.eventInfo().getIsAttend();
		this.eventDate = dto.eventInfo().getEventDate();
		this.location = dto.locationInfo().getLocation();
		this.address = dto.locationInfo().getAddress();
		this.latitude = dto.locationInfo().getLatitude();
		this.longitude = dto.locationInfo().getLongitude();
		this.note = dto.eventInfo().getNote();
	}
}
