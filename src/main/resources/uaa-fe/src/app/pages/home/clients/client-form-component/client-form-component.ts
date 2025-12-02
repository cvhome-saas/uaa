import {Component, Input, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-client-form-component',
  standalone: false,
  templateUrl: './client-form-component.html',
  styleUrl: './client-form-component.scss',
})
export class ClientFormComponent implements OnInit {
  form!: FormGroup;

  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      clientId: [''],
      clientIdIssuedAt: [''],
      clientName: [''],

      clientAuthenticationMethods: [[]],
      authorizationGrantTypes: [[]],

      redirectUris: this.fb.array([]),
      postLogoutRedirectUris: this.fb.array([]),

      scopes: [[]],

      requireProofKey: [false],
      requireAuthorizationConsent: [true],

      accessTokenTimeToLive: [''],
      refreshTokenTimeToLive: [''],
      reuseRefreshTokens: [false],
      idTokenSignatureAlgorithm: ['RS256'],
    });
  }

  @Input() set value(v: any) {
    if (!v) return;

    this.form.patchValue(v);

    this.redirectUris.clear();
    (v.redirectUris || []).forEach((x: string) => this.redirectUris.push(this.fb.group({value: x})));

    this.postLogoutRedirectUris.clear();
    (v.postLogoutRedirectUris || []).forEach((x: string) => this.postLogoutRedirectUris.push(this.fb.group({value: x})));
  }

  get redirectUris() {
    return this.form.get('redirectUris') as FormArray;
  }

  get postLogoutRedirectUris() {
    return this.form.get('postLogoutRedirectUris') as FormArray;
  }

  addRedirectUri() {
    this.redirectUris.push(this.fb.group({value: ''}));
  }

  removeRedirectUri(i: number) {
    this.redirectUris.removeAt(i);
  }

  addPostLogoutUri() {
    this.postLogoutRedirectUris.push(this.fb.group({value: ''}));
  }

  removePostLogoutUri(i: number) {
    this.postLogoutRedirectUris.removeAt(i);
  }

  submit() {
    console.log(this.form.value);
  }
}
